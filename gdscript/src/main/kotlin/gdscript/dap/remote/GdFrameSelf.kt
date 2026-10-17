package gdscript.dap.remote

import com.intellij.diagnostic.rethrowControlFlowException
import com.intellij.openapi.application.readAction
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.platform.dap.DapEvaluationContext
import com.intellij.platform.dap.DapEvaluationResult
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapStackFrame
import com.intellij.platform.dap.DapVariable
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.dap.protocol.DapRequestFailedException
import gdscript.GdScriptBundle
import gdscript.psi.GdAnnotationTl
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdFile
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdMethodDeclTl
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

private val LOG = logger<GdSelfCheckPolicy>()

/** The names and retry limits used by the self check and scope reads. */
internal object GdSelfCheckPolicy {
    const val MEMBERS_SCOPE_NAME: String = "Members"
    const val GLOBALS_SCOPE_NAME: String = "Globals"
    const val SELF_VARIABLE_NAME: String = "self"

    /** Bounds the self check and the start of new scope attempts. */
    val MEMBERS_RETRY_LIMIT: Duration = 2.seconds

    /** The wait between rejected scope requests, including the Members requests of the self check. */
    val MEMBERS_RETRY_DELAY: Duration = 10.milliseconds
}

/** The result of the `self` check of one stack frame. */
internal sealed interface GdFrameSelf {
    /** The Members scope contains [variable], the `self` of the frame. */
    data class Present(val variable: DapVariable) : GdFrameSelf

    /** The source rule rejects the frame, or the dump contains no self. */
    data object Absent : GdFrameSelf

    /** The source or the dump could not confirm self. */
    data object Unavailable : GdFrameSelf

    /** The source decision or the Members requests exceeded the total time limit. */
    data object TimedOut : GdFrameSelf
}

/**
 * Checks self before every evaluation in watches, recovery, and scene tree rows.
 * Godot ends its pause loop when evaluate runs in a frame without a script instance. The game then resumes.
 * See `remote_debugger.cpp`, `RemoteDebugger::debug`, the `evaluate` command.
 *
 * The source must permit self and the Members dump must contain self. Godot can send a dump from another frame.
 * Each call repeats both checks with its own time limit. A cancelled check does not change another caller's result.
 * Row code accepts this type instead of an unchecked frame or a general evaluator.
 * The factory supplies [watchStop] for dictionary reads. The self check does not use it.
 */
internal class GdSelfCheckedFrame(
    private val delegate: DapStackFrame,
    private val sourceSelf: suspend () -> GdFrameSourceSelf,
    private val checkLimit: Duration = GdSelfCheckPolicy.MEMBERS_RETRY_LIMIT,
    val watchStop: GdWatchStop? = null,
) : DapStackFrame by delegate {
    suspend fun DapSessionContext.checkSelf(): GdFrameSelf = withTimeoutOrNull(checkLimit) {
        when (sourceSelf()) {
            GdFrameSourceSelf.Present -> readSelfInMembers(delegate)
            GdFrameSourceSelf.Absent -> GdFrameSelf.Absent
            GdFrameSourceSelf.Unavailable -> GdFrameSelf.Unavailable
        }
    } ?: GdFrameSelf.TimedOut.also { LOG.debug("The self check of frame $id exceeded $checkLimit") }

    override suspend fun DapSessionContext.evaluate(expression: String): DapEvaluationResult =
        guarded { delegate.run { evaluate(expression) } }

    override suspend fun DapSessionContext.evaluate(expression: String, context: DapEvaluationContext): DapEvaluationResult =
        guarded { delegate.run { evaluate(expression, context) } }

    private suspend fun DapSessionContext.guarded(evaluate: suspend () -> DapEvaluationResult): DapEvaluationResult =
        when (checkSelf()) {
            is GdFrameSelf.Present -> evaluate()
            GdFrameSelf.Absent -> DapEvaluationResult.Error(GdScriptBundle.message("gdscript.debugger.error.evaluation.without.self"))
            GdFrameSelf.Unavailable ->
                DapEvaluationResult.Error(GdScriptBundle.message("gdscript.debugger.error.evaluation.self.unavailable"))

            GdFrameSelf.TimedOut -> DapEvaluationResult.Error(GdScriptBundle.message("gdscript.debugger.error.evaluation.self.timeout"))
        }
}

/** Reads the dump after the source decision. The caller must bound the requests with a time limit. */
internal suspend fun DapSessionContext.readSelfInMembers(
    frame: DapStackFrame,
    retryAllowed: () -> Boolean = { true },
    retryDelay: suspend () -> Unit = { delay(GdSelfCheckPolicy.MEMBERS_RETRY_DELAY) },
): GdFrameSelf {
    fun absent(reason: String): GdFrameSelf {
        LOG.debug("The dump of frame ${frame.id} has no supported self context: $reason")
        return GdFrameSelf.Absent
    }

    val members = try {
        frame.run { scopes() }.firstOrNull { it.name == GdSelfCheckPolicy.MEMBERS_SCOPE_NAME } ?: return absent("no Members scope")
    } catch (e: Throwable) {
        rethrowControlFlowException(e)
        LOG.debug("The scopes request of frame ${frame.id} failed", e)
        return GdFrameSelf.Unavailable
    }
    return try {
        val self = retryRejectedVariables(retryAllowed, retryDelay) { members.run { variables() } }
            .firstOrNull { it.name == GdSelfCheckPolicy.SELF_VARIABLE_NAME }
        if (self != null) GdFrameSelf.Present(self) else absent("no self variable in Members")
    } catch (e: DapRequestFailedException) {
        LOG.debug("Godot did not give the Members variables of frame ${frame.id}", e)
        GdFrameSelf.Unavailable
    } catch (e: Throwable) {
        rethrowControlFlowException(e)
        LOG.debug("The Members variables request of frame ${frame.id} failed", e)
        GdFrameSelf.Unavailable
    }
}

/** Returns true only when both the source and the dump show self. */
internal suspend fun DapSessionContext.hasSelfInMembers(frame: GdSelfCheckedFrame): Boolean = when (frame.run { checkSelf() }) {
    is GdFrameSelf.Present -> true
    GdFrameSelf.Absent, GdFrameSelf.Unavailable, GdFrameSelf.TimedOut -> false
}

/** The source decision for a frame. An unresolved source does not permit evaluation. */
internal enum class GdFrameSourceSelf { Present, Absent, Unavailable }

/** Runs [analyzeFrameLine] in a read action and drops the PSI. */
internal suspend fun frameSourceSelf(project: Project, frame: DapStackFrame): GdFrameSourceSelf = readAction {
    when (analyzeFrameLine(project, frame)) {
        is GdFrameLine.Present -> GdFrameSourceSelf.Present
        GdFrameLine.Absent -> GdFrameSourceSelf.Absent
        GdFrameLine.Unavailable -> GdFrameSourceSelf.Unavailable
    }
}

/**
 * Carries the [GdFrameSourceSelf] decision and the PSI for a present frame.
 * Use the PSI only inside the caller's read action.
 */
internal sealed interface GdFrameLine {
    class Present(val file: GdFile, val document: Document, val leaf: PsiElement) : GdFrameLine
    data object Absent : GdFrameLine
    data object Unavailable : GdFrameLine
}

/**
 * Checks whether the frame's source permits self through committed GDScript PSI. Call inside a read action.
 * Rejects static initializers, static functions, static variables, and lines inside or containing a lambda.
 * Static variables include their initializers and accessors.
 * Returns Unavailable for unresolved sources, uncommitted documents, empty lines, and comment-only lines.
 * A Present result permits the Members check but does not prove that Godot supplied self.
 */
internal fun analyzeFrameLine(project: Project, frame: DapStackFrame): GdFrameLine {
    fun unavailable(reason: String): GdFrameLine {
        LOG.debug("Cannot resolve GDScript frame ${frame.id}: $reason")
        return GdFrameLine.Unavailable
    }

    fun absent(reason: String): GdFrameLine {
        LOG.debug("GDScript frame ${frame.id} has no supported self context: $reason")
        return GdFrameLine.Absent
    }

    // The frame name identifies a static initializer without a source lookup.
    if (frame.name == "@static_initializer") return absent("static initializer")
    val source = frame.source ?: return unavailable("no source")
    // Built-in scripts use a resource path with a "::" suffix. The plugin cannot read their GDScript PSI.
    if (!source.isValid || "::" in source.path) return unavailable("unsupported source")
    val document = FileDocumentManager.getInstance().getDocument(source) ?: return unavailable("no document")
    val manager = PsiDocumentManager.getInstance(project)
    // Use committed PSI that matches the document text.
    if (!manager.isCommitted(document)) return unavailable("uncommitted document")
    val file = manager.getPsiFile(document) as? GdFile ?: return unavailable("not a GDScript file")

    // DAP lines start at 1. Document lines start at 0.
    val line = frame.startPosition.line - 1
    if (line !in 0 until document.lineCount) return unavailable("line out of range")
    val end = document.getLineEndOffset(line)
    var start = document.getLineStartOffset(line)
    val chars = document.charsSequence
    // Find the first code element. An empty line or a comment cannot establish self.
    while (start < end && chars[start].isWhitespace()) start++
    if (start == end) return unavailable("no code on the line")
    val leaf = file.findElementAt(start)
    if (leaf == null || leaf is PsiComment) return unavailable("no PSI code element")

    var hasAnnotation = false
    var hasDeclaration = false
    var lineLeaf: PsiElement? = leaf
    // Walk each leaf and its ancestors. An annotation is a sibling of its declaration.
    // A static keyword can follow the first leaf on the line.
    while (lineLeaf != null && lineLeaf.textRange.startOffset < end) {
        if (lineLeaf !is PsiComment) {
            var element: PsiElement? = lineLeaf
            while (element != null && element !is GdFile) {
                when (element) {
                    is GdFuncDeclEx -> return absent("lambda on the frame line")
                    is GdMethodDeclTl -> {
                        if (element.isStatic) return absent("static function")
                        hasDeclaration = true
                    }

                    is GdClassVarDeclTl -> {
                        if (element.isStatic) return absent("static variable")
                        hasDeclaration = true
                    }

                    is GdAnnotationTl -> hasAnnotation = true
                }
                element = element.parent
            }
        }
        lineLeaf = PsiTreeUtil.nextLeaf(lineLeaf)
    }

    // An annotation without a declaration on this line cannot establish self.
    if (hasAnnotation && !hasDeclaration) return unavailable("annotation without a declaration on the frame line")
    return GdFrameLine.Present(file, document, leaf)
}
