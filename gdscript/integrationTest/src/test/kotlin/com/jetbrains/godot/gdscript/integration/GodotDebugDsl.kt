package com.jetbrains.godot.gdscript.integration

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.execution.ProgramRunnerUtil.executeConfiguration
import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.executors.DefaultDebugExecutor.getDebugExecutorInstance
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.readAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.TextRange
import com.intellij.platform.dap.DapStartRequest
import com.intellij.psi.PsiDocumentManager
import com.intellij.testFramework.PlatformTestUtil.dispatchAllInvocationEventsInIdeEventQueue
import com.intellij.testFramework.PlatformTestUtil.waitWithEventsDispatching
import com.intellij.testFramework.assertEqualsToFile
import com.intellij.ui.SimpleTextAttributes
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebugSessionListener
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.XDebuggerTestUtil
import com.intellij.xdebugger.XDebuggerUtil
import com.intellij.xdebugger.evaluation.EvaluationMode
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XDebuggerTreeNodeHyperlink
import com.intellij.xdebugger.frame.XNamedValue
import com.intellij.xdebugger.frame.XStackFrame
import com.intellij.xdebugger.frame.XValue
import com.intellij.xdebugger.frame.XValueChildrenList
import com.intellij.xdebugger.frame.XValueContainer
import com.intellij.xdebugger.frame.XValueGroup
import com.jetbrains.godot.test.project.GodotProjectBuilder
import com.jetbrains.godot.test.project.GodotSourceBreakpoint
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.dap.GdScriptConfigurationType
import gdscript.dap.GdScriptRunConfiguration
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.CompletableFuture
import javax.swing.Icon
import kotlin.io.path.createDirectories
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

/** The whole budget of one GDScript debug test. */
val defaultDapTestTimeout: Duration = 5.minutes

/** The time that the editor gets to listen on the debug adapter port. */
val dapSocketTimeout: Duration = 1.minutes

/** The time that the game gets to start and to suspend at a breakpoint. */
val pauseTimeout: Duration = 2.minutes

/** The time that a stopped debug process gets to terminate. */
val processExitTimeout: Duration = 1.minutes

/**
 * The time that a step gets to settle after its pause, before the next command.
 *
 * Godot streams the frame's variables to the editor over its own connection to the paused game, after the
 * pause itself is already visible. A step that follows too soon after another one can race that stream: the
 * editor's debug adapter tracks the stream with one counter for the whole session, not one per pause, so an
 * overlap can leave the counter above zero forever. A `variables` request then never gets an answer.
 *
 * The wait is a workaround for this Godot engine race, not a fix of it. It buys the time for one pause's
 * stream to finish before a step asks for the next one.
 */
val stepSettleDelay: Duration = 300.milliseconds

/** The headless game needs these arguments because Godot does not forward the editor's headless mode. */
fun GodotProjectBuilder.dapRunArgs() = runArgs("--headless", "--display-driver", "headless", "--audio-driver", "Dummy")

/**
 * Runs [body] against one GDScript debug session of [editor].
 *
 * This function owns the session. It stops the session and waits for the debug process, also when [body]
 * throws. A failure of [body] is reported first, and a failure of the shutdown is suppressed into it.
 */
suspend fun <T> godotDebug(editor: GodotEditorFixture, body: suspend GodotDebugSession.() -> T): T {
    val session = GodotDebugSession(editor)
    val result: T
    var bodyFailure: Throwable? = null
    try {
        result = session.body()
    } catch (failure: Throwable) {
        bodyFailure = failure
        failure.addSuppressed(IllegalStateException("Godot editor output:\n${editor.editorOutput}"))
        throw failure
    } finally {
        try {
            stopDebugSession(editor.project)
        } catch (stopFailure: Throwable) {
            if (bodyFailure == null) {
                throw stopFailure
            }
            bodyFailure.addSuppressed(stopFailure)
        }
    }
    session.compareDumpsWithGold()
    return result
}

/** The type and the text of one evaluated GDScript expression. */
data class GodotValue(val type: String?, val value: String)

class GodotDebugSession internal constructor(val editor: GodotEditorFixture) {
    private val project get() = editor.project
    private val dumps = StringBuilder()

    /** Toggles a line breakpoint at the `# <bp:[marker]>` marker of the project. */
    suspend fun breakpoint(marker: String) {
        val location = editor.sourceBreakpoint(marker)
        val file = editor.virtualFile(location.path)
        withContext(Dispatchers.EDT) {
            // GdScriptLineBreakpointType is internal to the plugin, so the test framework picks the type.
            checkNotNull(XDebuggerTestUtil.toggleBreakpoint(project, file, location.line.asZeroBasedLine())) {
                "No breakpoint type accepts ${location.path}:${location.line}"
            }
        }
    }

    /**
     * Plays the main scene of the project through a GDScript run configuration.
     *
     * The whole IDE chain runs: the run configuration, the debug adapter connection, the breakpoint and the
     * source position of the frame.
     */
    suspend fun launch() {
        withContext(Dispatchers.EDT) {
            awaitDapSocket()
            val type = ConfigurationTypeUtil.findConfigurationType(GdScriptConfigurationType::class.java)
            val runManager = RunManager.getInstanceAsync(project)
            val settings = runManager.createConfiguration("GDScript debug", type.factory)
            (settings.configuration as GdScriptRunConfiguration).json = launchJson(editor.dapPort)
            runManager.addConfiguration(settings)
            executeConfiguration(settings, getDebugExecutorInstance())
        }
    }

    /**
     * Waits until the session suspends, and checks the position against the `# <bp:[at]>` marker.
     *
     * A null [at] accepts any position, which a step command needs.
     */
    suspend fun waitForPause(at: String? = null) {
        val expected = at?.let { editor.sourceBreakpoint(it) }
        withContext(Dispatchers.EDT) {
            var suspended: XDebugSession? = null
            waitWithEventsDispatching(
                { "The session did not suspend at ${expected?.let { "${it.path}:${it.line}" } ?: "a breakpoint"}" },
                {
                    suspended = XDebuggerManager.getInstance(project).currentSession?.takeIf { it.isSuspended }
                    suspended != null
                },
                pauseTimeout.inWholeSeconds.toInt(),
            )
            val session = checkNotNull(suspended) { "No suspended session" }
            val position = checkNotNull(session.currentPosition) { "The suspended session reported no position" }
            if (expected != null) {
                val file = editor.virtualFile(expected.path)
                check(position.file == file) { "Suspended in ${position.file}, expected $file" }
                check(position.line == expected.line.asZeroBasedLine()) {
                    "Suspended at line ${position.line + 1}, expected ${expected.line}"
                }
            }
        }
    }

    /**
     * Opens the evaluate window of the suspended session and runs [body] in it.
     *
     * The window is the code fragment that the debug process builds from the source position of the frame,
     * so a lookup in it proves what the paused frame offers.
     *
     * A lookup that boils down to a single item is otherwise inserted straight away, leaving nothing to
     * inspect. This block therefore turns the automatic insertion off, and it restores the previous value.
     * The value stays inside the block, because an LSP completion test needs the platform default.
     */
    suspend fun <T> immediate(body: suspend GodotImmediateWindow.() -> T): T {
        val session = suspendedSession()
        val settings = CodeInsightSettings.getInstance()
        val previous = settings.AUTOCOMPLETE_ON_CODE_COMPLETION
        settings.AUTOCOMPLETE_ON_CODE_COMPLETION = false
        try {
            return GodotImmediateWindow(editor, session).body()
        } finally {
            settings.AUTOCOMPLETE_ON_CODE_COMPLETION = previous
        }
    }

    /**
     * Resumes the session and waits until the game runs again.
     *
     * The command returns at the resume, not at the next pause. A test that expects another pause therefore
     * calls [waitForPause] after it. The wait removes the race where a check of the suspended state still
     * sees the previous pause.
     */
    suspend fun resume() {
        val session = suspendedSession()
        session.runAndAwait("resume", SessionSignal.RESUMED) { session.resume() }
    }

    /** Steps over the current line and waits for the next pause. */
    suspend fun stepOver() {
        val session = suspendedSession()
        session.runAndAwait("step over", SessionSignal.PAUSED) { session.stepOver(false) }
    }

    /** Steps into the call on the current line and waits for the next pause. */
    suspend fun stepInto() {
        val session = suspendedSession()
        session.runAndAwait("step into", SessionSignal.PAUSED) { session.stepInto() }
    }

    suspend fun evaluate(expression: String): GodotValue {
        val session = suspendedSession()
        val result = withContext(Dispatchers.Default) { XDebuggerTestUtil.evaluate(session, expression) }
        check(result.second == null) { "The evaluation of '$expression' failed: ${result.second}" }
        val value = checkNotNull(result.first) { "The evaluation of '$expression' gave no value" }
        val presentation = presentationOf(value)
        return GodotValue(presentation.myType, presentation.myValue)
    }

    /**
     * Adds the position of the paused frame to the gold text.
     *
     * The line number is 1-based, as the gutter and the `# <bp:name>` marker count. The source text makes the
     * dump readable, and a trim removes the GDScript tab indent, which is invisible in a gold file.
     */
    suspend fun dumpFrame() {
        val frame = suspendedFrame()
        val position = checkNotNull(frame.sourcePosition) { "The frame reported no position" }
        val sourceLine = readAction {
            val document = checkNotNull(FileDocumentManager.getInstance().getDocument(position.file)) {
                "The file ${position.file.name} has no document"
            }
            document.getText(TextRange(document.getLineStartOffset(position.line), document.getLineEndOffset(position.line)))
        }
        dumps.appendLine("--> ${position.file.name}:${position.line + 1} // ${sourceLine.trim()}")
        dumps.appendLine("${frameName(frame)} in ${position.file.name}")
    }

    /**
     * Adds the variables of the paused frame to the gold text.
     *
     * The children of a DAP frame are its scopes, so a scope name stands in brackets and its variables stand
     * one step deeper. [depth] adds the children of a variable: 0 prints the variables alone, and 1 also
     * prints the children of each variable.
     *
     * [skipScopes] drops a scope by name. The default drops the scope of the engine globals, which holds
     * every autoload singleton of the project. Godot leaves it empty for a project without an autoload, so
     * the scope adds one line and no information, and it grows with the project.
     */
    suspend fun dumpVariables(depth: Int = 0, skipScopes: Set<String> = setOf(GLOBALS_SCOPE)) {
        val frame = suspendedFrame()
        for (scope in collectChildren(frame).groups) {
            if (scope.name in skipScopes) continue
            dumps.appendLine("${indent(1)}[${scope.name}]")
            dumpContainer(scope, depth, level = 2)
        }
    }

    /** Returns the collected groups and values of the paused frame. */
    internal suspend fun frameChildren(): GodotChildren = collectChildren(suspendedFrame())

    private suspend fun dumpContainer(container: XValueContainer, depth: Int, level: Int) {
        val children = collectChildren(container)
        for (group in children.groups) {
            dumps.appendLine("${indent(level)}[${group.name}]")
            dumpContainer(group, depth, level + 1)
        }
        for (value in children.values) {
            val presentation = presentationOf(value)
            val name = (value as? XNamedValue)?.name ?: presentation.myName
            val marker = if (presentation.myHasChildren) "+" else ""
            val type = presentation.myType?.takeIf { it.isNotEmpty() }?.let { "{$it} " } ?: ""
            dumps.appendLine("${indent(level)}$marker$name = $type${presentation.myValue}")
            if (depth > 0 && presentation.myHasChildren) {
                dumpContainer(value, depth - 1, level + 1)
            }
        }
    }

    internal fun compareDumpsWithGold() {
        if (dumps.isEmpty()) return
        val goldFile = editor.goldDirectory.resolve("${editor.testName}.gold")
        val actual = maskObjectIds(dumps.toString())
        val projectPath = editor.projectDir.toString()
        val alternatives = setOf(projectPath.replace('\\', '/'), projectPath.replace('/', '\\'))
        val pattern = Regex(alternatives.joinToString("|") { Regex.escape(it) })
        val maskedActual = pattern.replace(actual) { "<ABSOLUTE_PATH>" }
        goldFile.parent?.createDirectories()
        assertEqualsToFile("The gold output differs", goldFile.toFile(), maskedActual)
    }

    private fun suspendedFrame(): XStackFrame =
        checkNotNull(suspendedSession().currentStackFrame) { "The suspended session reported no frame" }

    private fun suspendedSession(): XDebugSession {
        val session = checkNotNull(XDebuggerManager.getInstance(project).currentSession) { "No debug session runs" }
        check(session.isSuspended) { "The debug session does not hang on a breakpoint" }
        return session
    }

    /**
     * Waits for the debug adapter port before the run configuration starts.
     *
     * A refused connection causes a bounded launch failure, not a hang. A headless dialog closes at once, so
     * the cost is a hard failure after a few short attempts.
     */
    private fun awaitDapSocket() {
        waitWithEventsDispatching(
            { "The Godot debug adapter never listened on port ${editor.dapPort}" },
            {
                try {
                    Socket().use { it.connect(InetSocketAddress(LOOPBACK_ADDRESS, editor.dapPort), SOCKET_PROBE_TIMEOUT_MS) }
                    true
                } catch (_: IOException) {
                    false
                }
            },
            dapSocketTimeout.inWholeSeconds.toInt(),
        )
    }
}

class GodotImmediateWindow internal constructor(
    private val editor: GodotEditorFixture,
    private val session: XDebugSession,
) {
    /** Returns the lookup strings that the window offers for [prefix], caret is at the end of  [prefix]. */
    suspend fun complete(prefix: String = ""): List<String> = withContext(Dispatchers.EDT) {
        val position = checkNotNull(session.currentPosition) { "The suspended session reported no position" }
        val project = editor.project
        val expression = XDebuggerUtil.getInstance()
            .createExpression(prefix, GdLanguage, null, EvaluationMode.EXPRESSION)
        val document = session.debugProcess.editorsProvider
            .createDocument(project, expression, position, EvaluationMode.EXPRESSION)

        val fragment = checkNotNull(PsiDocumentManager.getInstance(project).getPsiFile(document)) {
            "The evaluate window has no PSI file"
        }
        val fragmentFile = checkNotNull(fragment.virtualFile) { "A physical fragment must have a VirtualFile" }
        val codeInsight = editor.codeInsight
        codeInsight.configureFromExistingVirtualFile(fragmentFile)
        codeInsight.editor.caretModel.moveToOffset(document.textLength)

        codeInsight.completeBasic()?.map { it.lookupString }.orEmpty()
    }
}

/**
 * Stops the session and waits for its process to terminate.
 *
 * The variables view keeps asking the adapter for the scopes of the frame while the session hangs on a
 * breakpoint. Killing the editor first breaks that socket mid-request, so the session goes first.
 *
 * The shutdown also runs when the test body fails or times out, so it ignores the cancellation of the caller.
 */
private suspend fun stopDebugSession(project: Project) {
    val session = XDebuggerManager.getInstance(project).currentSession ?: return
    val processHandler = session.debugProcess.processHandler
    awaitFrameRequests(session)
    withContext(NonCancellable + Dispatchers.EDT) {
        session.stop()
        waitWithEventsDispatching(
            { "The debug session did not stop" },
            { processHandler.isProcessTerminated },
            processExitTimeout.inWholeSeconds.toInt(),
        )
    }
}

private enum class SessionSignal { PAUSED, RESUMED }

/**
 * Runs [action] and waits for the session event that [signal] names.
 *
 * The platform reports both events to a session listener, which is the signal that the adapter answered. A
 * poll of the suspended state cannot tell one pause from the next one.
 */
private suspend fun XDebugSession.runAndAwait(commandName: String, signal: SessionSignal, action: () -> Unit) {
    val arrived = CompletableDeferred<Unit>()
    val disposable = Disposer.newDisposable("Godot debug $commandName")
    try {
        addSessionListener(
            object : XDebugSessionListener {
                override fun sessionPaused() {
                    if (signal == SessionSignal.PAUSED) arrived.complete(Unit)
                }

                override fun sessionResumed() {
                    if (signal == SessionSignal.RESUMED) arrived.complete(Unit)
                }
            },
            disposable,
        )
        withContext(Dispatchers.EDT) { action() }
        checkNotNull(withTimeoutOrNull(pauseTimeout) { arrived.await() }) { "The session did not report the $commandName" }
        if (signal == SessionSignal.PAUSED) delay(stepSettleDelay)
    } finally {
        Disposer.dispose(disposable)
    }
}

private fun frameName(frame: XStackFrame): String =
    XDebuggerTestUtil.getFramePresentation(frame).substringBefore(", ")

internal class GodotChildren(val groups: List<XValueGroup>, val values: List<XValue>)

/**
 * Returns the children of [container], and fails when the adapter reported an error.
 *
 * An empty result without an error means an empty container. A silent empty dump would hide a defect.
 */
internal suspend fun collectChildren(container: XValueContainer): GodotChildren =
    withContext(Dispatchers.Default) {
        val node = GodotCompositeNode(describe(container))
        container.computeChildren(node)
        node.children()
    }

private fun describe(container: XValueContainer): String = when (container) {
    is XStackFrame -> "the frame"
    is XValueGroup -> "the scope ${container.name}"
    is XNamedValue -> "the value ${container.name}"
    else -> "a ${container.javaClass.simpleName}"
}

/**
 * Collects one level of the debugger tree, and keeps the groups.
 *
 * A DAP frame adds one group for each scope, through `addTopGroup`, and a group is no `XValue`. The platform
 * node `XTestCompositeNode` keeps only the values, so `XDebuggerTestUtil.collectChildren` gives an empty
 * list for a DAP frame. This node keeps the groups too.
 *
 * The class implements `XCompositeNode`, which the platform reserves for itself. A test needs the same
 * access as the debugger tree, and the platform test framework implements the interface for the same reason.
 */
private class GodotCompositeNode(private val subject: String) : XCompositeNode {
    private val groups = mutableListOf<XValueGroup>()
    private val values = mutableListOf<XValue>()
    private val result = CompletableFuture<Pair<GodotChildren, String?>>()

    override fun addChildren(children: XValueChildrenList, last: Boolean) {
        groups.addAll(children.topGroups)
        values.addAll(children.topValues)
        for (index in 0 until children.size()) {
            values.add(children.getValue(index))
        }
        groups.addAll(children.bottomGroups)
        if (last) complete(null)
    }

    @Deprecated("Deprecated in Java")
    override fun tooManyChildren(remaining: Int) = complete(null)

    override fun setAlreadySorted(alreadySorted: Boolean) = Unit

    override fun setErrorMessage(errorMessage: String) = complete(errorMessage)

    override fun setErrorMessage(errorMessage: String, link: XDebuggerTreeNodeHyperlink?) = complete(errorMessage)

    override fun setMessage(
        message: String,
        icon: Icon?,
        attributes: SimpleTextAttributes,
        link: XDebuggerTreeNodeHyperlink?,
    ) = Unit

    /** Waits for the answer of the adapter, and fails when the adapter gave none or reported an error. */
    fun children(): GodotChildren {
        val (children, error) = checkNotNull(XDebuggerTestUtil.waitFor(result, XDebuggerTestUtil.TIMEOUT_MS.toLong())) {
            "The adapter did not answer the request about the children of $subject"
        }
        check(error == null) { "The adapter reported an error for the children of $subject: $error" }
        return children
    }

    private fun complete(errorMessage: String?) {
        result.complete(GodotChildren(groups.toList(), values.toList()) to errorMessage)
    }
}

internal suspend fun presentationOf(value: XValue) =
    withContext(Dispatchers.Default) { XDebuggerTestUtil.computePresentation(value) }

private fun indent(level: Int): String = "    ".repeat(level)

/**
 * Replaces the identity of a Godot object with one stable token.
 *
 * Godot prints an object value as `<EncodedObjectAsID#-9223370334789362620>`, and the number differs at
 * every pause of one run. The type of the value stays in the dump, so the mask hides nothing that a test
 * asserts.
 */
private fun maskObjectIds(text: String): String = OBJECT_ID.replace(text, "<OBJECT_ID>")

/**
 * Waits until the adapter answered every pending request about the frame.
 *
 * The variables view of the session tab asks the adapter for the scopes of the frame. The request travels one
 * queue in the order of arrival, so an answer to a later request proves that the earlier one is done. A
 * `stop` while a request waits closes the socket, and the write then fails on a background thread.
 *
 * The event queue runs first, because the view sends its request from the event queue.
 */
private suspend fun awaitFrameRequests(session: XDebugSession) {
    if (!session.isSuspended) return
    @Suppress("ForbiddenInSuspectContextMethod")
    withContext(NonCancellable + Dispatchers.EDT) { dispatchAllInvocationEventsInIdeEventQueue() }
    val frame = session.currentStackFrame ?: return
    withContext(NonCancellable) { XDebuggerTestUtil.collectChildren(frame) }
}

/**
 * Builds the launch arguments of the run configuration.
 *
 * The text is JSON, and the configuration parser also accepts a comment. The template of the production
 * factory carries comments, so a test must not derive its arguments from it.
 */
private fun launchJson(dapPort: Int): String = """
    {
      "request": "${DapStartRequest.Launch.name}",
      "debugServer": $dapPort
    }
    """.trimIndent()

private fun GodotEditorFixture.sourceBreakpoint(marker: String): GodotSourceBreakpoint =
    checkNotNull(breakpoints[marker]) {
        "The project has no '# <bp:$marker>' marker. It has ${breakpoints.keys}"
    }

private fun GodotEditorFixture.virtualFile(path: String) =
    checkNotNull(files[path]?.virtualFile) { "The project has no $path file" }

/**
 * Converts the line number that a project marker states into the one the platform takes.
 *
 * A marker counts lines the way the editor gutter shows them, from 1. Every `XDebugger` API counts from 0.
 */
private fun Int.asZeroBasedLine(): Int = this - 1

private const val LOOPBACK_ADDRESS = "127.0.0.1"
private const val SOCKET_PROBE_TIMEOUT_MS = 500

/** The name that Godot gives the scope of the autoload singletons. */
private const val GLOBALS_SCOPE = "Globals"
private val OBJECT_ID = Regex("<[A-Za-z_][A-Za-z0-9_]*#-?\\d+>")
