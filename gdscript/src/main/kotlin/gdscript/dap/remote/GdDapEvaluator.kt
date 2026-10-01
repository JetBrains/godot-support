package gdscript.dap.remote

import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.platform.dap.DapEvaluationResult
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.xdebugger.DapXDebuggerPresentationFactory
import com.intellij.platform.dap.xdebugger.DefaultDapXDebuggerEvaluator
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.concurrency.AppExecutorUtil
import com.intellij.xdebugger.XSourcePosition
import com.intellij.xdebugger.evaluation.ExpressionInfo
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator
import gdscript.psi.GdAttributeEx
import gdscript.psi.GdCallEx
import gdscript.psi.GdGetDecl
import gdscript.psi.GdInheritanceIdRef
import gdscript.psi.GdInheritanceSubIdRef
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdNodePath
import gdscript.psi.GdPrimaryEx
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdSetDecl
import gdscript.psi.GdTypeHintRef
import gdscript.psi.GdTypes
import gdscript.psi.utils.GdClassUtil
import org.jetbrains.annotations.VisibleForTesting
import org.jetbrains.concurrency.Promise
import org.jetbrains.concurrency.resolvedPromise

private val LOG = logger<GdDapEvaluator>()

/**
 * Evaluates expressions in the paused frame through the self check.
 * Selects a hover range only where the paused frame can see the hovered name.
 */
internal class GdDapEvaluator(
    private val executor: DapSessionExecutor,
    private val factory: DapXDebuggerPresentationFactory,
    private val checkedFrame: GdSelfCheckedFrame,
) : XDebuggerEvaluator() {
    private val platformRangeEvaluator = DefaultDapXDebuggerEvaluator(executor, factory, checkedFrame)

    /**
     * Evaluates through the self check and returns a [GdDapXValue].
     * Stores the expression on the result row so child rows can derive their expressions.
     * Shows the result before an optional object probe updates its presentation.
     * Keeps the text that the user entered unchanged.
     */
    override fun evaluate(expression: String, callback: XEvaluationCallback, expressionPosition: XSourcePosition?) {
        executor.evaluateWithCompletion(callback) { answer ->
            when (val result = checkedFrame.run { evaluate(expression) }) {
                is DapEvaluationResult.Error -> answer.errorOccurred(result.errorMessage)
                is DapEvaluationResult.Success -> {
                    val owner = GdOwnerExpression(expression)
                    val row = GdDapXValue(factory, executor, result.variable, owner = owner, frame = checkedFrame)
                    answer.evaluated(row)
                    if (GdObjectProbe.isProbeTarget(result.variable, owner, allowInferredType = true) && hasSelfInMembers(checkedFrame)) {
                        val presentations = with(GdObjectProbe) { load(checkedFrame, listOf(expression)) }
                        row.updatePresentation(presentations?.singleOrNull())
                    }
                }
            }
        }
    }

    /**
     * Checks the frame scope, then applies the GDScript range rules.
     */
    @Suppress("IncorrectParentDisposable")
    override fun getExpressionInfoAtOffsetAsync(
        project: Project, document: Document, offset: Int, sideEffectsAllowed: Boolean,
    ): Promise<ExpressionInfo?> = ReadAction.nonBlocking<GdHoverRangeOutcome> {
        hoverRangeAtOffset(project, checkedFrame, document, offset, sideEffectsAllowed)
    }
        .withDocumentsCommitted(project)
        .expireWith(project)
        .submit(AppExecutorUtil.getAppExecutorService())
        .thenAsync { result ->
            when (result) {
                is GdHoverRangeOutcome.Range -> resolvedPromise(result.info)
                GdHoverRangeOutcome.OutOfFrameScope, GdHoverRangeOutcome.NoRange, null -> resolvedPromise(null)
                GdHoverRangeOutcome.PlatformRule -> platformRangeEvaluator.getExpressionInfoAtOffsetAsync(
                    project,
                    document,
                    offset,
                    sideEffectsAllowed
                )
            }
        }
}

/**
 * Selects the source text to evaluate at [offset] within the paused frame's scope.
 * Node paths also provide rewritten evaluation text through [ExpressionInfo].
 * Call inside a read action.
 */
@VisibleForTesting
internal fun hoverRangeAtOffset(
    project: Project, frame: GdSelfCheckedFrame, document: Document, offset: Int, sideEffectsAllowed: Boolean,
): GdHoverRangeOutcome {
    // 1. Match the paused frame source and scope.
    val line = when (val result = analyzeFrameLine(project, frame)) {
        is GdFrameLine.Present -> result
        GdFrameLine.Absent, GdFrameLine.Unavailable -> return GdHoverRangeOutcome.OutOfFrameScope
    }
    if (document !== line.document || offset !in 0 until document.textLength) {
        LOG.debug("Cannot resolve GDScript frame: hover document or offset does not match the frame")
        return GdHoverRangeOutcome.OutOfFrameScope
    }
    val leaf = line.file.findElementAt(offset) ?: return GdHoverRangeOutcome.OutOfFrameScope
    val frameScope = scopeAt(line.leaf)
    val hoverScope = scopeAt(leaf)
    val scopeMatches = hoverScope.owner == frameScope.owner ||
        (hoverScope.owner == hoverScope.classOwner && hoverScope.classOwner == frameScope.classOwner)
    if (!scopeMatches) return GdHoverRangeOutcome.OutOfFrameScope

    // 2. Select a node path or an attribute chain.
    // A path can contain the token, or it can start the receiver of the hovered member.
    val path = PsiTreeUtil.getParentOfType(leaf, GdNodePath::class.java, false)
    val member = PsiTreeUtil.getParentOfType(leaf, GdRefIdRef::class.java, false)
    val attribute = member?.parent as? GdAttributeEx
    // Include the node path through the hovered attribute, such as `$Player.position.x`.
    // Stop at a call or subscript because it breaks the attribute chain.
    val receiverNodePath = if (member != null && leaf.node.elementType == GdTypes.IDENTIFIER) {
        var receiver: PsiElement? = attribute?.expr
        while (receiver is GdAttributeEx) receiver = receiver.expr
        (receiver as? GdPrimaryEx)?.nodePath
    } else {
        null
    }

    val nodePath = path ?: receiverNodePath
    if (nodePath != null) {
        val selected = if (path != null) nodePath.parent as? GdPrimaryEx ?: nodePath
        else attribute ?: return GdHoverRangeOutcome.NoRange
        val call = selected.parent as? GdCallEx
        // A trailing call needs permission to cause side effects.
        if (call != null && !sideEffectsAllowed) return GdHoverRangeOutcome.NoRange
        val end = call?.textRange?.endOffset ?: selected.textRange.endOffset
        val range = TextRange(nodePath.textRange.startOffset, end)
        val expression = "get_node_or_null(${nodePathLiteral(nodePath.text)})" +
            document.getText(TextRange(nodePath.textRange.endOffset, end))
        return GdHoverRangeOutcome.Range(ExpressionInfo(range, expression, document.getText(range)))
    }
    // 3. Filter GDScript tokens before the platform selects ordinary names.
    if (PsiTreeUtil.getParentOfType(leaf, GdTypeHintRef::class.java, false) != null ||
        PsiTreeUtil.getParentOfType(leaf, GdInheritanceIdRef::class.java, false) != null ||
        PsiTreeUtil.getParentOfType(leaf, GdInheritanceSubIdRef::class.java, false) != null ||
        leaf.node.elementType != GdTypes.IDENTIFIER && leaf.node.elementType != GdTypes.SELF
    ) return GdHoverRangeOutcome.NoRange
    return GdHoverRangeOutcome.PlatformRule
}

private class HoverScope(val owner: PsiElement, val classOwner: PsiElement)

/**
 * Uses the nearest method or accessor with a body as the scope owner, or the enclosing class otherwise.
 * Lambdas do not create a separate owner for this comparison.
 */
private fun scopeAt(leaf: PsiElement): HoverScope {
    val classOwner = GdClassUtil.getOwningClassElement(leaf)
    val owner = PsiTreeUtil.findFirstParent(leaf) {
        it is GdMethodDeclTl || it is GdGetDecl && it.stmtOrSuite != null || it is GdSetDecl && it.stmtOrSuite != null
    } ?: classOwner
    return HoverScope(owner, classOwner)
}

internal sealed interface GdHoverRangeOutcome {
    /** The source or scope check rejects the position. */
    data object OutOfFrameScope : GdHoverRangeOutcome

    /** The token has no range, or a node-path call needs side-effect permission. */
    data object NoRange : GdHoverRangeOutcome

    /** The node path or its supported attribute chain has [info]. */
    class Range(val info: ExpressionInfo) : GdHoverRangeOutcome

    /** The platform selects an ordinary name or `self` after the GDScript checks. */
    data object PlatformRule : GdHoverRangeOutcome
}

/**
 * Converts a `$` or `%` node path to a string literal for `get_node_or_null`.
 * Removes `$` and keeps `%` inside the resulting string literal.
 * For example, `$Player` becomes `"Player"`, and `%Player` becomes `"%Player"`.
 * Preserves existing quotes and escape sequences.
 * Adds double quotes to an unquoted path.
 * Godot Expression interprets `\a`, `\v`, and backslash line continuation differently from GDScript.
 */
private fun nodePathLiteral(source: String): String {
    val prefix = if (source[0] == '%') "%" else ""
    val path = source.substring(1)
    return if (path[0] == '"' || path[0] == '\'') "${path[0]}$prefix${path.substring(1)}" else "\"$prefix$path\""
}
