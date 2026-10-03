package com.jetbrains.godot.gdscript.dap

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.dap.DapEvaluationResult
import com.intellij.platform.dap.DapExceptionInfo
import com.intellij.platform.dap.DapInlineValueLocator
import com.intellij.platform.dap.DapInlineValueLookup
import com.intellij.platform.dap.DapScope
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.DapSessionStoppedException
import com.intellij.platform.dap.DapStackFrame
import com.intellij.platform.dap.DapStackFramesChunk
import com.intellij.platform.dap.DapStopDetails
import com.intellij.platform.dap.DapStructuredVariable
import com.intellij.platform.dap.DapThread
import com.intellij.platform.dap.DapThreadState
import com.intellij.platform.dap.DapVariable
import com.intellij.platform.dap.FrameId
import com.intellij.platform.dap.SourcePosition
import com.intellij.platform.dap.StackFrameType
import com.intellij.platform.dap.StepInTargetId
import com.intellij.platform.dap.StepSize
import com.intellij.platform.dap.TextPosition
import com.intellij.platform.dap.ThreadId
import com.intellij.platform.dap.ValueKind
import com.intellij.platform.dap.VariableBatch
import com.intellij.platform.dap.VariableFilter
import com.intellij.platform.dap.xdebugger.AbstractDapXValue
import com.intellij.platform.dap.xdebugger.DapXDebuggerPresentationFactory
import com.intellij.platform.dap.xdebugger.DefaultDapXDebuggerPresentationFactory
import com.intellij.testFramework.LightVirtualFile
import com.intellij.testFramework.junit5.RegistryKey
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.fixture.projectFixture
import com.intellij.ui.SimpleTextAttributes
import com.intellij.xdebugger.XDebuggerBundle
import com.intellij.xdebugger.XExpression
import com.intellij.xdebugger.XSourcePosition
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator.XEvaluationCallback
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XDebuggerTreeNodeHyperlink
import com.intellij.xdebugger.frame.XFullValueEvaluator
import com.intellij.xdebugger.frame.XNamedValue
import com.intellij.xdebugger.frame.XValue
import com.intellij.xdebugger.frame.XValueChildrenList
import com.intellij.xdebugger.frame.XValueGroup
import com.intellij.xdebugger.frame.XValueNode
import com.intellij.xdebugger.frame.XValuePlace
import com.intellij.xdebugger.frame.presentation.XValuePresentation
import com.jetbrains.dap.protocol.DapConnectionClosedException
import com.jetbrains.dap.protocol.DapRequestFailedException
import com.jetbrains.dap.protocol.RequestCancelledByPeerException
import com.jetbrains.dap.protocol.ExceptionDetails
import com.jetbrains.dap.protocol.StopReason
import com.jetbrains.dap.protocol.StoppedEventArguments
import com.jetbrains.dap.protocol.VariableAttribute
import gdscript.GdScriptBundle
import gdscript.dap.GdUnavailableEvaluator
import gdscript.dap.remote.GdChildChunkXValueGroup
import gdscript.dap.remote.GdChildRange
import gdscript.dap.remote.GdChildRowsLoad
import gdscript.dap.remote.GdChildrenXValueGroup
import gdscript.dap.remote.GdDapPresentationFactory
import gdscript.dap.remote.GdDapStackFrame
import gdscript.dap.remote.GdDapXScope
import gdscript.dap.remote.GdDapXValue
import gdscript.dap.remote.GdFrameSourceSelf
import gdscript.dap.remote.GdNodeDescriptor
import gdscript.dap.remote.GdNodeDetection
import gdscript.dap.remote.GdNodeMembersXValue
import gdscript.dap.remote.GdOwnerExpression
import gdscript.dap.remote.GdSceneTreeNodeXValue
import gdscript.dap.remote.GdSelfCheckPolicy
import gdscript.dap.remote.GdSelfCheckedFrame
import gdscript.dap.remote.computeChildrenWithCompletion
import gdscript.dap.remote.computePromiseWithCompletion
import gdscript.dap.remote.detectFrameNode
import gdscript.dap.remote.evaluateWithCompletion
import gdscript.dap.remote.loadChildRows
import gdscript.dap.remote.loadVariablesBounded
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.supervisorScope
import org.jetbrains.concurrency.CancellablePromise
import org.jetbrains.concurrency.Promise
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.swing.Icon
import kotlin.time.TestTimeSource

@TestApplication
class GdCompletionBridgeTest {
    enum class Consumer { NODE, CALLBACK, PROMISE }
    enum class PresentationOutcome { FAILED, SUCCESS, MISSING, UNAVAILABLE }
    enum class NodeRow { ROOT, SCENE, CHILD, PARENT }

    @ParameterizedTest
    @EnumSource(value = Consumer::class, names = ["CALLBACK"])
    fun testCancellationBeforeEntry(consumer: Consumer): Unit = withBridge(consumer) { executor, bridge ->
        var entered = false
        bridge.submit {
            entered = true
            it()
        }
        executor.last.cancelAndJoin()
        assertFalse(entered)
        bridge.assertFallback(cancelled = true)
    }

    @ParameterizedTest
    @EnumSource(value = Consumer::class, names = ["NODE", "PROMISE"])
    fun testCancellationDuringSuspension(consumer: Consumer): Unit = withBridge(consumer) { executor, bridge ->
        val entered = CompletableDeferred<Unit>()
        bridge.submit {
            entered.complete(Unit)
            awaitCancellation()
        }
        executor.last.start()
        entered.await()
        executor.last.cancelAndJoin()
        bridge.assertFallback(cancelled = true)
    }

    @ParameterizedTest
    @EnumSource(value = Consumer::class, names = ["NODE", "CALLBACK"])
    fun testSubmissionAfterSessionStop(consumer: Consumer): Unit = withBridge(consumer) { executor, bridge ->
        executor.stopped = true
        bridge.submit { error("The work must not start") }
        bridge.assertFallback()
    }

    @ParameterizedTest
    @EnumSource(Consumer::class)
    fun testRequestFailure(consumer: Consumer): Unit = withBridge(consumer) { executor, bridge ->
        bridge.submit { throw DapRequestFailedException("request failed") }
        executor.runLast()
        bridge.assertFallback()
        if (consumer == Consumer.NODE) assertEquals(listOf("request failed"), bridge.node.errors)
    }

    @Test
    fun testConnectionClosureQuietlyCompletesTheNode(): Unit = withExecutor { executor ->
        val node = RecordingNode()
        executor.computeChildrenWithCompletion(node, LOG, "test children", errorMessage = { it.message!! }) {
            throw connectionClosed()
        }
        executor.runLast()
        assertEquals(1, node.batches.size)
        assertEquals(1, node.terminalCount)
        assertTrue(node.errors.isEmpty())
    }

    @ParameterizedTest
    @EnumSource(value = Consumer::class, names = ["NODE", "CALLBACK"])
    fun testNoDuplicateTerminalAnswer(consumer: Consumer): Unit = withBridge(consumer) { executor, bridge ->
        bridge.submit { answer ->
            answer()
            answer()
            throw IllegalStateException("probe failed")
        }
        executor.runLast()
        assertEquals(listOf("success"), bridge.answers)
        assertTrue(bridge.node.errors.isEmpty())
    }

    @ParameterizedTest
    @CsvSource("false, FAILED", "true, FAILED", "false, SUCCESS", "true, SUCCESS", "false, MISSING", "true, MISSING", "true, UNAVAILABLE")
    fun testPresentationCompletesOnce(debugNode: Boolean, outcome: PresentationOutcome): Unit = withExecutor { executor ->
        var described = false
        val value = if (outcome == PresentationOutcome.SUCCESS) nodeValue(executor, debugNode)
        else unresolvedNodeValue(executor, debugNode) { expression ->
            when (outcome) {
                PresentationOutcome.FAILED -> throw IllegalStateException("request failed")
                PresentationOutcome.UNAVAILABLE -> DapEvaluationResult.Error("missing")
                PresentationOutcome.MISSING -> {
                    if (debugNode && described) numberedAnswer(expression, listOf(variable("0", "bool", "false")))
                    else {
                        described = true
                        DapEvaluationResult.Success(variable("result", "Nil", "<null>"))
                    }
                }
                PresentationOutcome.SUCCESS -> error("Unexpected unresolved row")
            }
        }
        val node = RecordingValueNode()
        value.computePresentation(node, XValuePlace.TREE)
        val work = executor.last
        executor.runLast()
        val propagated = runCatching { work.await() }.exceptionOrNull()
        when (outcome) {
            PresentationOutcome.FAILED -> {
                assertEquals(null, propagated)
                assertEquals(listOf(GdScriptBundle.message("gdscript.debugger.scene.tree.error.evaluation.failed", "request failed")),
                             node.messages)
            }
            PresentationOutcome.SUCCESS -> {
                assertEquals(null, propagated)
                assertTrue(node.messages.isEmpty())
            }
            PresentationOutcome.MISSING -> {
                assertEquals(null, propagated)
                val message = if (debugNode) "null"
                else GdScriptBundle.message("gdscript.debugger.scene.tree.error.node.not.found")
                assertEquals(listOf(message), node.messages)
            }
            PresentationOutcome.UNAVAILABLE -> {
                assertEquals(listOf(GdScriptBundle.message("gdscript.debugger.node.parent.unavailable")), node.messages)
            }
        }
        assertEquals(1, node.presentations)
        assertEquals(listOf(outcome == PresentationOutcome.SUCCESS), node.expandable)
    }

    @ParameterizedTest
    @EnumSource(value = NodeRow::class, names = ["SCENE", "CHILD"])
    fun testPresentationAfterSessionStop(row: NodeRow): Unit = withExecutor { executor ->
        val value = nodeValue(executor, row)
        executor.stopped = true
        val node = RecordingValueNode()
        value.computePresentation(node, XValuePlace.TREE)
        assertEquals(listOf(GdScriptBundle.message("gdscript.debugger.error.evaluation.session.stopped")), node.messages)
        assertEquals(1, node.presentations)
        assertEquals(listOf(false), node.expandable)
    }

    @ParameterizedTest
    @EnumSource(value = NodeRow::class, names = ["ROOT", "PARENT", "SCENE", "CHILD"])
    fun testPresentationWhileSessionQuiesced(row: NodeRow): Unit = withExecutor { executor ->
        val closed = connectionClosed()
        val value = nodeValue(executor, row) { throw closed }
        executor.quiesced = true
        val node = RecordingValueNode()
        value.computePresentation(node, XValuePlace.TREE)
        executor.runLast()
        when (row) {
            NodeRow.ROOT, NodeRow.PARENT -> {
                assertEquals(listOf(GdScriptBundle.message("gdscript.debugger.error.evaluation.session.stopped")), node.messages)
                assertEquals(listOf(false), node.expandable)
            }
            NodeRow.SCENE, NodeRow.CHILD -> {
                assertTrue(node.messages.isEmpty())
                assertEquals(listOf(true), node.expandable)
            }
        }
        assertEquals(1, node.presentations)
    }

    @ParameterizedTest
    @EnumSource(value = NodeRow::class, names = ["SCENE", "CHILD"])
    fun testPresentationCancellationBeforeEntry(row: NodeRow): Unit = withExecutor { executor ->
        var evaluated = false
        val value = nodeValue(executor, row) {
            evaluated = true
            error("The evaluation must not start")
        }
        val node = RecordingValueNode()
        value.computePresentation(node, XValuePlace.TREE)
        executor.last.cancelAndJoin()
        assertFalse(evaluated)
        assertEquals(listOf(GdScriptBundle.message("gdscript.debugger.error.cancelled")), node.messages)
        assertEquals(1, node.presentations)
        assertEquals(listOf(false), node.expandable)
    }

    private fun unresolvedNodeValue(
        executor: DapSessionExecutor, debugNode: Boolean, evaluation: suspend (String) -> DapEvaluationResult
    ): XNamedValue {
        val frame = GdSelfCheckedFrame(TestFrame { evaluation(it) }, { GdFrameSourceSelf.Present })
        return if (debugNode) GdNodeMembersXValue.parentOf(42, executor, frame, FACTORY)
        else GdSceneTreeNodeXValue("Root", executor, frame, FACTORY)
    }

    @Test
    fun testScopeRetriesUntilSuccess(): Unit = withExecutor { executor ->
        val failures = ArrayDeque(listOf("not ready", "still not ready"))
        val expected = listOf(variable("health", "int", "100"))
        val scope = testScope {
            if (failures.isNotEmpty()) throw DapRequestFailedException(failures.removeFirst())
            expected
        }
        val node = RecordingNode()
        GdDapXScope(FACTORY, executor, scope, 0, scopeLoader = { scope, retryAllowed ->
            loadVariablesBounded(scope, retryAllowed = retryAllowed, retryDelay = {})
        }).computeChildren(node)
        executor.last.await()
        assertEquals(1, node.terminalCount)
        assertTrue(node.errors.isEmpty())
        assertEquals(listOf("health"), node.values.map { it.name })
    }

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun testOtherScopeFailuresShowAnErrorWithoutRetry(closed: Boolean): Unit = withExecutor { executor ->
        val failure = if (closed) connectionClosed() else IOException("read failed")
        val scope = testScope { throw failure }
        val node = RecordingNode()
        GdDapXScope(FACTORY, executor, scope, 0, scopeLoader = { scope, retryAllowed ->
            loadVariablesBounded(scope, retryAllowed = retryAllowed, retryDelay = { error("The failure must not retry") })
        }).computeChildren(node)
        executor.last.await()
        assertEquals(listOf(GdScriptBundle.message("gdscript.debugger.error.evaluation.failed",
            failure.message ?: GdScriptBundle.message("gdscript.debugger.error.unknown"))), node.errors)
        assertEquals(1, node.terminalCount)
    }

    @Test
    @RegistryKey(key = "dap.timeout.evaluate", value = "1")
    fun testScopeTimeoutShowsAnErrorWithoutRetry(): Unit = withExecutor { executor ->
        val node = RecordingNode()
        val scope = testScope { awaitCancellation() }
        GdDapXScope(FACTORY, executor, scope, 0, scopeLoader = { scope, retryAllowed ->
            loadVariablesBounded(scope, retryAllowed = retryAllowed, retryDelay = { error("A timeout must not retry") })
        }).computeChildren(node)
        executor.last.await()
        assertEquals(listOf(GdScriptBundle.message("gdscript.debugger.error.evaluation.failed",
            GdScriptBundle.message("gdscript.debugger.error.command.timed.out"))), node.errors)
        assertEquals(1, node.terminalCount)
    }

    @Test
    fun testStructuredVariablesDoNotRetry(): Unit = withExecutor { executor ->
        val failure = DapRequestFailedException("nested read failed")
        val value = object : DapStructuredVariable by structuredVariable() {
            override fun DapSessionContext.loadVariables(filter: VariableFilter?, batchSize: Int): Flow<VariableBatch> = flow {
                throw failure
            }
        }
        val work = executor.postAsync { loadVariablesBounded(value) }
        assertSame(failure, runCatching { work.await() }.exceptionOrNull())
    }

    @Test
    fun testPeerCancellationReportsAnErrorWithoutRetry(): Unit = withExecutor { executor ->
        val failure = RequestCancelledByPeerException::class.java.getDeclaredConstructor(String::class.java)
            .apply { isAccessible = true }.newInstance("variables")
        val node = RecordingNode()
        val scope = testScope { throw failure }
        GdDapXScope(FACTORY, executor, scope, 0).computeChildren(node)
        executor.runLast()
        assertEquals(listOf(GdScriptBundle.message("gdscript.debugger.error.cancelled")), node.errors)
        assertEquals(1, node.terminalCount)
    }

    @ParameterizedTest
    @CsvSource("false, false", "true, false", "true, true")
    fun testScopeCancellationStopsRequestsAndCompletesTheNode(
        duringWait: Boolean, obsolete: Boolean,
    ): Unit = withExecutor { executor ->
        val entered = CompletableDeferred<Unit>()
        val scope = testScope {
            check(!entered.isCompleted) { "The cancelled scope started another request" }
            if (duringWait) throw DapRequestFailedException("not ready")
            entered.complete(Unit)
            awaitCancellation()
        }
        val node = RecordingNode()
        GdDapXScope(FACTORY, executor, scope, 0, scopeLoader = { scope, retryAllowed ->
            loadVariablesBounded(scope, retryAllowed = retryAllowed, retryDelay = {
                entered.complete(Unit)
                awaitCancellation()
            })
        }).computeChildren(node)
        executor.last.start()
        entered.await()
        node.obsolete = obsolete
        executor.last.cancelAndJoin()
        assertEquals(1, node.terminalCount)
        assertTrue(node.values.isEmpty())
        assertEquals(if (obsolete) emptyList<String>() else listOf(GdScriptBundle.message("gdscript.debugger.error.cancelled")), node.errors)
    }

    @ParameterizedTest
    @CsvSource("cancelled, false", "stopped, false", "stopped, true", "active, true")
    fun testInterruptedScopeCompletesWithoutStartingARequest(state: String, obsolete: Boolean): Unit = withExecutor { executor ->
        val scope = testScope { error("The request must not start") }
        val node = RecordingNode().apply { this.obsolete = obsolete }
        executor.stopped = state == "stopped"
        GdDapXScope(FACTORY, executor, scope, 0).computeChildren(node)
        when (state) {
            "cancelled" -> executor.last.cancelAndJoin()
            "active" -> executor.last.await()
        }
        assertEquals(1, node.terminalCount)
        assertTrue(node.values.isEmpty())
        val key = if (state == "stopped") "gdscript.debugger.error.evaluation.session.stopped" else "gdscript.debugger.error.cancelled"
        assertEquals(if (obsolete) emptyList<String>() else listOf(GdScriptBundle.message(key)), node.errors)
    }

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun testObsoleteScopeStopsRetryAttempts(duringWait: Boolean): Unit = withExecutor { executor ->
        val entered = CompletableDeferred<Unit>()
        val release = CompletableDeferred<Unit>()
        val node = RecordingNode()
        val scope = testScope {
            check(!node.obsolete) { "The obsolete scope started another request" }
            if (!duringWait) node.obsolete = true
            throw DapRequestFailedException("not ready")
        }
        GdDapXScope(FACTORY, executor, scope, 0, scopeLoader = { scope, retryAllowed ->
            loadVariablesBounded(scope, retryAllowed = retryAllowed, retryDelay = {
                entered.complete(Unit)
                release.await()
            })
        }).computeChildren(node)
        executor.last.start()
        if (duringWait) {
            entered.await()
            node.obsolete = true
            release.complete(Unit)
        }
        executor.last.await()
        assertEquals(1, node.terminalCount)
        assertTrue(node.errors.isEmpty())
        assertTrue(node.values.isEmpty())
    }

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun testScopeRetryLimitDoesNotCutAnInFlightAttempt(succeeds: Boolean): Unit = withExecutor { executor ->
        var requests = 0
        val clock = TestTimeSource()
        val entered = CompletableDeferred<Unit>()
        val release = CompletableDeferred<Unit>()
        val expected = listOf(variable("health", "int"))
        val scope = testScope {
            if (++requests == 1) throw DapRequestFailedException("not ready")
            if (requests == 2) {
                entered.complete(Unit)
                release.await()
                if (!succeeds) throw DapRequestFailedException("deadline reached")
            }
            expected
        }
        val node = RecordingNode()
        GdDapXScope(FACTORY, executor, scope, 0, scopeLoader = { scope, retryAllowed ->
            loadVariablesBounded(scope, retryAllowed = retryAllowed, retryDelay = {}, timeSource = clock)
        }).computeChildren(node)
        val work = executor.last
        work.start()
        select<Unit> {
            entered.onAwait {}
            work.onAwait { error("The retry attempt must start") }
        }
        clock += GdSelfCheckPolicy.MEMBERS_RETRY_LIMIT + GdSelfCheckPolicy.MEMBERS_RETRY_DELAY
        assertFalse(work.isCompleted)
        release.complete(Unit)
        work.await()
        assertEquals(1, node.terminalCount)
        assertEquals(if (succeeds) listOf("health") else emptyList<String>(), node.values.map { it.name })
        assertEquals(if (succeeds) emptyList<String>() else
            listOf(GdScriptBundle.message("gdscript.debugger.error.evaluation.failed", "deadline reached")), node.errors)
    }

    @ParameterizedTest
    @CsvSource("Locals, 0, false, true", "Locals, 0, true, false", "Members, 1, false, false")
    fun testPlainScopesKeepPlatformValuesAndInlineContext(
        name: String, index: Int, expensive: Boolean, autoExpand: Boolean,
    ): Unit = withExecutor { executor ->
        val frame = TestFrame { error("Unexpected evaluation") }.apply { source = LightVirtualFile("frame.gd") }
        val variables = listOf(structuredVariable("player"), variable("health", "int", "100"))
        val scope = testScope(name, frame, expensive) { variables }
        val lookup = object : DapInlineValueLookup {
            override fun findVariablePosition(variableName: String): XSourcePosition? = null
        }
        val locator = object : DapInlineValueLocator {
            override suspend fun createLookup(framePosition: XSourcePosition): DapInlineValueLookup {
                assertSame(frame.source, framePosition.file)
                assertEquals(0, framePosition.line)
                return lookup
            }
        }
        val group = GdDapXScope(FACTORY, executor, scope, index, inlineValueLocator = locator)
        assertEquals(autoExpand, group.isAutoExpand)
        if (!autoExpand) return@withExecutor
        val node = RecordingNode()
        group.computeChildren(node)
        executor.last.await()
        assertEquals(1, node.terminalCount)
        assertTrue(node.errors.isEmpty())
        assertEquals(listOf("player", "health"), node.values.map { it.name })
        val contexts = node.values.map { (it as AbstractDapXValue).inlineValueContext }
        assertNotNull(contexts.first())
        contexts.forEach {
            assertSame(contexts.first(), it)
            assertSame(lookup, it!!.lookup)
        }
        val shown = RecordingValueNode()
        node.values.first().computePresentation(shown, XValuePlace.TREE)
        assertEquals(listOf("value"), shown.rendered)
        assertEquals(listOf(true), shown.expandable)
    }

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun testPresentationFactoryGuardsEveryFrameScope(withSelf: Boolean): Unit = withExecutor { executor ->
        val frame = TestFrame { expression ->
            assertTrue(withSelf)
            numberedAnswer(expression, listOf(variable("0", "bool", "false")))
        }.apply {
            scopeNames = listOf("Locals", "Members", "Globals")
        }
        val factory = GdDapPresentationFactory(projectFixture.get(), null)
        val node = RecordingNode()
        GdDapStackFrame(factory, executor, frame.thread, GdSelfCheckedFrame(frame, {
            if (withSelf) GdFrameSourceSelf.Present else GdFrameSourceSelf.Absent
        })).computeChildren(node)
        executor.last.await()
        val groups = node.batches.flatMap { it.first.topGroups }
        assertEquals(if (withSelf) listOf("Locals") else frame.scopeNames, groups.map { it.name })
        assertEquals(if (withSelf) listOf("root", "self") else emptyList<String>(),
                     node.values.map { it.name })
        assertEquals(1, node.terminalCount)
        assertTrue(node.errors.isEmpty())
        for (group in groups) {
            val children = RecordingNode()
            group.computeChildren(children)
            executor.last.await()
            assertEquals(1, children.terminalCount)
            assertTrue(children.errors.isEmpty())
            assertEquals(if (group.name == "Locals") listOf("above") else listOf("self"), children.values.map { it.name })
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun testFrameLocalsProbeObjectsOnlyWithSelf(withSelf: Boolean): Unit = withExecutor { executor ->
        val release = CompletableDeferred<Unit>()
        val evaluations = mutableListOf<String>()
        var probing = false
        val frame = TestFrame { expression ->
            evaluations.add(expression)
            if (probing) {
                release.await()
                numberedAnswer(expression, probeFields("Player:<Node#42>", "Node"))
            }
            else numberedAnswer(expression, listOf(variable("0", "bool", "false")))
        }.apply {
            scopeNames = listOf("Locals", "Members")
            localVariables = listOf(structuredVariable("player"))
            memberVariables = if (withSelf) listOf(variable("self", "Object")) else emptyList()
        }
        val factory = GdDapPresentationFactory(projectFixture.get(), null)
        val node = RecordingNode()
        GdDapStackFrame(factory, executor, frame.thread, GdSelfCheckedFrame(frame, { GdFrameSourceSelf.Present })).computeChildren(node)
        executor.last.await()
        val locals = node.batches.flatMap { it.first.topGroups }.single { it.name == "Locals" }
        probing = true
        val childrenShown = CompletableDeferred<Unit>()
        val children = RecordingNode { _, last -> if (last) childrenShown.complete(Unit) }
        locals.computeChildren(children)
        val work = executor.last
        try {
            work.start()
            childrenShown.await()
            assertEquals(listOf("player"), children.values.map { it.name })
            val shown = RecordingValueNode()
            children.values.single().computePresentation(shown, XValuePlace.TREE)
            assertEquals(listOf("value"), shown.rendered)
            release.complete(Unit)
            work.await()
            assertEquals(if (withSelf) listOf("value", "Node  Player:<Node#42>") else listOf("value"), shown.rendered)
            assertEquals(1, children.terminalCount)
            assertTrue(node.errors.isEmpty())
            assertTrue(children.errors.isEmpty())
            if (!withSelf) assertTrue(evaluations.isEmpty())
        }
        finally {
            release.complete(Unit)
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun testCategoryRecoveryRefreshesTheDisplayedRow(obsolete: Boolean): Unit = withExecutor { executor ->
        val entered = CompletableDeferred<Unit>()
        val release = CompletableDeferred<Unit>()
        var recovering = true
        val nested = structuredVariable("Members", "Category", listOf(variable("health", "int", "100")))
        val frame = GdSelfCheckedFrame(TestFrame { expression ->
            if (recovering) {
                recovering = false
                entered.complete(Unit)
                release.await()
                numberedAnswer(expression, listOf(structuredVariable("0", children = listOf(nested))) +
                    probeFields("Player:<Node#42>", "Node", start = 1))
            }
            else {
                numberedAnswer(expression, listOf(variable("0", "int", "100")) + probeFields("100", "int", valid = false, start = 1))
            }
        }, { GdFrameSourceSelf.Present })
        val category = GdDapXValue(FACTORY, executor,
            structuredVariable("Members", "Category", listOf(variable("base.gd/player", "int", "42"))),
            owner = GdOwnerExpression("self"), frame = frame)
        val children = RecordingNode()
        category.computeChildren(children)
        val work = executor.last
        try {
            work.start()
            entered.await()
            assertEquals(1, children.terminalCount)
            val row = children.values.single()
            val shown = RecordingValueNode()
            row.computePresentation(shown, XValuePlace.TREE)
            assertEquals(listOf("42"), shown.rendered)
            assertEquals(listOf(false), shown.expandable)
            shown.obsolete = obsolete
            release.complete(Unit)
            work.await()
            assertSame(row, children.values.single())
            assertEquals(if (obsolete) listOf("42") else listOf("42", "Node  Player:<Node#42>"), shown.rendered)
            assertEquals(if (obsolete) listOf(false) else listOf(false, true), shown.expandable)
            assertEquals(1, children.terminalCount)
            val fresh = RecordingValueNode()
            row.computePresentation(fresh, XValuePlace.TREE)
            assertEquals(listOf("Node  Player:<Node#42>"), fresh.rendered)
            assertEquals(listOf(true), fresh.expandable)
            val objectChildren = RecordingNode()
            row.computeChildren(objectChildren)
            executor.last.await()
            assertEquals(listOf("Members"), objectChildren.values.map { it.name })
            val properties = RecordingNode()
            objectChildren.values.single().computeChildren(properties)
            executor.last.await()
            assertEquals(listOf("health"), properties.values.map { it.name })
            assertEquals(1, properties.terminalCount)
        }
        finally {
            release.complete(Unit)
        }
    }

    @ParameterizedTest
    @CsvSource("true, false", "false, false", "true, true")
    fun testChildProbeRefreshesTheCorrectRows(presentBeforeAnswer: Boolean, obsolete: Boolean): Unit = withExecutor { executor ->
        val entered = CompletableDeferred<Unit>()
        val release = CompletableDeferred<Unit>()
        val frame = GdSelfCheckedFrame(TestFrame { expression ->
            entered.complete(Unit)
            release.await()
            val requested = Regex("str\\(([^,]*?)\\), is_instance_valid").findAll(expression).map { it.groupValues[1] }.toList()
            val fields = requested.flatMapIndexed { index, child ->
                when (child) {
                    "(player).get(\"left\")" -> probeFields("Left:<Node#1>", "Node", start = index * 5)
                    "(player).get(\"right\")" -> probeFields("Right:<Node2D#2>", "Node2D", start = index * 5)
                    else -> probeFields("<null>", "Nil", valid = false, start = index * 5)
                }
            }
            numberedAnswer(expression, fields)
        }, { GdFrameSourceSelf.Present })
        val parent = GdDapXValue(FACTORY, executor, structuredVariable(children = listOf(
            structuredVariable("left"), variable("health", "int", "100"), structuredVariable("right"))),
            owner = GdOwnerExpression("player"), frame = frame)
        val children = RecordingNode()
        parent.computeChildren(children)
        executor.last.await()
        assertEquals(listOf("left", "health", "right"), children.values.map { it.name })
        assertEquals(1, children.terminalCount)
        val work = executor.last
        val shown = children.values.map { RecordingValueNode() }
        try {
            work.start()
            entered.await()
            if (presentBeforeAnswer) {
                children.values.zip(shown).forEach { (row, node) -> row.computePresentation(node, XValuePlace.TREE) }
                shown.forEach { it.obsolete = obsolete }
            }
            release.complete(Unit)
            work.await()
            if (!presentBeforeAnswer) {
                children.values.zip(shown).forEach { (row, node) -> row.computePresentation(node, XValuePlace.TREE) }
            }
            val expected = listOf("Node  Left:<Node#1>", "100", "Node2D  Right:<Node2D#2>")
            assertEquals(if (obsolete) listOf("value", "100", "value") else expected,
                         shown.map { it.rendered.last() })
            assertEquals(if (presentBeforeAnswer && !obsolete) listOf(2, 1, 2) else listOf(1, 1, 1),
                         shown.map { it.presentations })
            val fresh = children.values.map { row -> RecordingValueNode().also { row.computePresentation(it, XValuePlace.TREE) } }
            assertEquals(expected, fresh.map { it.rendered.single() })
            assertEquals(listOf(true, false, true), fresh.map { it.expandable.single() })
        }
        finally {
            release.complete(Unit)
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun testOverlappingFrameRequestsCompleteInEitherOrder(secondFirst: Boolean): Unit = withExecutor { executor ->
        val entered = List(2) { CompletableDeferred<Unit>() }
        val release = List(2) { CompletableDeferred<Unit>() }
        var request = 0
        val frame = TestFrame(onScopes = {
            val index = request++
            entered[index].complete(Unit)
            release[index].await()
        }) { error("Unexpected evaluation") }.apply { scopeName = "Locals" }
        val stackFrame = GdDapStackFrame(FACTORY, executor, frame.thread, GdSelfCheckedFrame(frame, { GdFrameSourceSelf.Absent }))
        val completionOrder = mutableListOf<Int>()
        val nodes = List(2) { index -> RecordingNode { _, last -> if (last) completionOrder.add(index) } }
        try {
            stackFrame.computeChildren(nodes[0])
            val first = executor.last
            first.start()
            entered[0].await()
            stackFrame.computeChildren(nodes[1])
            val second = executor.last
            second.start()
            entered[1].await()
            assertFalse(first.isCompleted)
            assertFalse(second.isCompleted)
            assertTrue(nodes.all { it.batches.isEmpty() })
            val order = if (secondFirst) listOf(1, 0) else listOf(0, 1)
            val jobs = listOf(first, second)
            release[order[0]].complete(Unit)
            jobs[order[0]].await()
            assertTrue(nodes[order[1]].batches.isEmpty())
            assertEquals(listOf(order[0]), completionOrder)
            release[order[1]].complete(Unit)
            jobs[order[1]].await()
            assertEquals(order, completionOrder)
            for (node in nodes) {
                assertEquals(1, node.terminalCount)
                assertTrue(node.errors.isEmpty())
                assertTrue(node.values.isEmpty())
                val groups = node.batches.flatMap { it.first.topGroups }
                assertEquals(listOf("Locals"), groups.map { it.name })
            }
        }
        finally {
            release.forEach { it.complete(Unit) }
        }
    }

    @Test
    fun testFrameNodeDetectionPreservesIdentity(): Unit = withExecutor { executor ->
        val frame = evaluationFrame { expression ->
            numberedAnswer(expression, listOf(
                variable("0", "bool", "true"),
                variable("1", "int", "73"),
                variable("2", "StringName", "Node2D"),
                variable("3", "StringName", "Player"),
            ))
        }
        val detection = executor.postAsync { detectFrameNode(frame) }.await() as GdNodeDetection.Node
        val node = detection.node
        assertEquals(73L, node.objectId)
        assertEquals("Player", node.nodeName)
        assertEquals("Node2D", node.className)
    }

    @Test
    fun testFailedExceptionValueKeepsTheExceptionMessage(): Unit = withExecutor { executor ->
        val frame = TestFrame { expression ->
            if (expression == "exception") DapEvaluationResult.Error("value unavailable")
            else numberedAnswer(expression, listOf(variable("0", "bool", "false")))
        }
        frame.thread.state = DapThreadState.Paused(DapStopDetails(
            emptyList(), DapExceptionInfo("The exception message", ExceptionDetails(evaluateName = "exception")),
            StoppedEventArguments(reason = StopReason.Exception)
        ))
        val node = RecordingNode()
        GdDapStackFrame(FACTORY, executor, frame.thread, GdSelfCheckedFrame(frame, { GdFrameSourceSelf.Present }))
            .computeChildren(node)
        executor.last.await()
        assertEquals(listOf("The exception message"), node.messages)
        assertEquals(listOf("root", "self"), node.values.map { it.name })
        assertTrue(node.errors.isEmpty())
        assertEquals(1, node.terminalCount)
    }

    @Test
    fun testFrameEmissionFailureShowsPartialRowsAndAnError(): Unit = withExecutor { executor ->
        val frame = TestFrame { error("Unexpected evaluation") }.apply { scopeNames = listOf("Locals", "Members") }
        val factory = object : DefaultDapXDebuggerPresentationFactory(null) {
            override fun createScope(executor: DapSessionExecutor, scope: DapScope, index: Int): XValueGroup {
                if (scope.name == "Members") throw IllegalStateException("scope emission failed")
                return super.createScope(executor, scope, index)
            }
        }
        val node = RecordingNode()
        GdDapStackFrame(factory, executor, frame.thread, GdSelfCheckedFrame(frame, { GdFrameSourceSelf.Absent }))
            .computeChildren(node)
        executor.runLast()
        assertEquals(listOf("Locals"), node.batches.flatMap { it.first.topGroups }.map { it.name })
        assertEquals(listOf(GdScriptBundle.message("gdscript.debugger.error.unknown")), node.errors)
        assertEquals(1, node.terminalCount)
        assertFalse(node.batches.first().second)
        assertTrue(node.batches.last().second)
    }

    @ParameterizedTest
    @CsvSource("false, false", "true, false", "false, true", "true, true")
    fun testChildGroupLoadFailureShowsAnErrorAndCompletes(chunk: Boolean, unexpected: Boolean): Unit = withExecutor { executor ->
        val frame = evaluationFrame {
            if (unexpected) throw IllegalStateException("load failed")
            DapEvaluationResult.Error("load failed")
        }
        val group = if (chunk) GdChildChunkXValueGroup(GdChildRange(17, 1), 42, executor, frame, FACTORY, sceneTree = false)
        else GdChildrenXValueGroup(42, executor, frame, FACTORY)
        val node = RecordingNode()
        group.computeChildren(node)
        executor.runLast()
        val key = if (chunk && !unexpected) "gdscript.debugger.scene.tree.error.descriptor.failed"
        else "gdscript.debugger.scene.tree.error.evaluation.failed"
        assertEquals(listOf(GdScriptBundle.message(key, "load failed")), node.errors)
        assertTrue(node.values.isEmpty())
        assertEquals(1, node.terminalCount)
    }

    @Test
    fun testUnsuspendedFrameShowsAnErrorAndCompletes(): Unit = withExecutor { executor ->
        val frame = TestFrame { error("Unexpected evaluation") }
        frame.thread.state = DapThreadState.Running
        val node = RecordingNode()
        GdDapStackFrame(FACTORY, executor, frame.thread, GdSelfCheckedFrame(frame, { GdFrameSourceSelf.Present }))
            .computeChildren(node)
        assertEquals(listOf(XDebuggerBundle.message("debugger.frames.dialog.message.not.available.for.unsuspended")), node.errors)
        assertTrue(node.values.isEmpty())
        assertTrue(node.batches.flatMap { it.first.topGroups }.isEmpty())
        assertEquals(1, node.terminalCount)
    }

    @ParameterizedTest
    @CsvSource("0, 0", "17, 100", "17, 101")
    fun testChildRowsChooseLeavesOrLazyGroups(start: Int, length: Int): Unit = withExecutor { executor ->
        val frame = evaluationFrame { expression ->
            val fields = (0 until length).flatMap { index -> listOf(
                variable((index * 3).toString(), "int", (index + 1).toString()),
                variable((index * 3 + 1).toString(), "String", "Child${start + index}"),
                variable((index * 3 + 2).toString(), "String", "Node")
            ) }
            numberedAnswer(expression, fields)
        }
        val work = executor.postAsync { loadChildRows(frame, 42, GdChildRange(start, length), executor, FACTORY, sceneTree = false) }
        val rows = (work.await() as GdChildRowsLoad.Success).children
        if (length <= 100) {
            assertEquals((start until start + length).map { "Child$it" }, (0 until rows.size()).map { rows.getName(it) })
            assertTrue(rows.topGroups.isEmpty())
        }
        else {
            assertEquals(0, rows.size())
            assertEquals(listOf("[17..116]", "[117..117]"), rows.topGroups.map { it.name })
        }
    }

    @Test
    fun testNestedChunkExpansionBuildsOnlyGroups(): Unit = withExecutor { executor ->
        val frame = evaluationFrame { expression -> numberedAnswer(expression, emptyList()) }
        val node = RecordingNode()
        GdChildChunkXValueGroup(GdChildRange(17, 10_000), 42, executor, frame, FACTORY, sceneTree = true).computeChildren(node)
        executor.last.await()
        val groups = node.batches.flatMap { it.first.topGroups }
        assertEquals((0 until 100).map { GdChildRange(17 + it * 100, 100).label }, groups.map { it.name })
        assertTrue(node.values.isEmpty())
        assertTrue(node.errors.isEmpty())
        assertEquals(1, node.terminalCount)
    }

    @Test
    fun testEvaluatorCancellationCompletesTheCallback(): Unit = withExecutor { executor ->
        val entered = CompletableDeferred<Unit>()
        val frame = TestFrame {
            entered.complete(Unit)
            awaitCancellation()
        }
        val callback = RecordingCallback()
        GdDapStackFrame(FACTORY, executor, frame.thread, GdSelfCheckedFrame(frame, { GdFrameSourceSelf.Present }))
            .evaluator.evaluate("health", callback, null)
        executor.last.start()
        entered.await()
        executor.last.cancelAndJoin()
        assertEquals(listOf(GdScriptBundle.message("gdscript.debugger.error.cancelled")), callback.errors)
        assertEquals(1, callback.answerCount)
    }

    @Test
    fun testEvaluatorSuccessSurvivesProbeCancellation(): Unit = withExecutor { executor ->
        var evaluations = 0
        val probing = CompletableDeferred<Unit>()
        val frame = TestFrame {
            if (++evaluations == 1) DapEvaluationResult.Success(structuredVariable())
            else {
                probing.complete(Unit)
                awaitCancellation()
            }
        }
        val callback = RecordingCallback()
        GdDapStackFrame(FACTORY, executor, frame.thread, GdSelfCheckedFrame(frame, { GdFrameSourceSelf.Present }))
            .evaluator.evaluate("player", callback, null)
        executor.last.start()
        probing.await()
        assertEquals(1, callback.values.size)
        executor.last.cancelAndJoin()
        assertEquals(1, callback.answerCount)
        assertTrue(callback.errors.isEmpty())
    }

    @ParameterizedTest
    @EnumSource(value = GdFrameSourceSelf::class, names = ["Absent", "Unavailable"])
    @NullSource
    internal fun testSourceRejectionOrTimeoutCompletesTheCallback(source: GdFrameSourceSelf?): Unit = withExecutor { executor ->
        val frame = TestFrame(onScopes = { error("The source rule must block the dump request") }) { error("Unexpected evaluation") }
        val callback = RecordingCallback()
        GdDapStackFrame(FACTORY, executor, frame.thread, GdSelfCheckedFrame(frame, { source ?: awaitCancellation() }))
            .evaluator.evaluate("health", callback, null)
        executor.runLast()
        assertEquals(1, callback.answerCount)
        val key = when (source) {
            GdFrameSourceSelf.Absent -> "gdscript.debugger.error.evaluation.without.self"
            GdFrameSourceSelf.Unavailable -> "gdscript.debugger.error.evaluation.self.unavailable"
            null -> "gdscript.debugger.error.evaluation.self.timeout"
            GdFrameSourceSelf.Present -> error("Unexpected source decision")
        }
        assertEquals(listOf(GdScriptBundle.message(key)), callback.errors)
    }

    @Test
    fun testEvaluationWithoutAFrameCompletesTheCallback() {
        val callback = RecordingCallback()
        GdUnavailableEvaluator().evaluate("health", callback, null)
        assertEquals(listOf(GdScriptBundle.message("gdscript.debugger.error.evaluation.frame.unavailable")), callback.errors)
        assertEquals(1, callback.answerCount)
    }

    @ParameterizedTest
    @CsvSource("false, success", "true, success", "false, cancelled", "true, cancelled", "false, stopped", "true, stopped")
    fun testExpressionPromiseCompletesOnSuccessCancellationOrStop(debugNode: Boolean, scenario: String): Unit = withExecutor { executor ->
        val value = nodeValue(executor, debugNode)
        executor.stopped = scenario == "stopped"
        val promise = value.calculateEvaluationExpression() as CancellablePromise<XExpression>
        when (scenario) {
            "success" -> {
                executor.runLast()
                assertEquals(Promise.State.SUCCEEDED, promise.state)
                assertEquals("instance_from_id(42)", promise.blockingGet(0, TimeUnit.SECONDS)?.expression)
            }
            "cancelled" -> {
                executor.last.cancelAndJoin()
                assertTrue(promise.isCancelled)
            }
            "stopped" -> assertEquals(Promise.State.REJECTED, promise.state)
        }
    }

    private fun nodeValue(executor: DapSessionExecutor, debugNode: Boolean): XNamedValue {
        val descriptor = GdNodeDescriptor(objectId = 42, nodeName = "Test", className = "Node")
        val frame = GdSelfCheckedFrame(TestFrame { error("Unexpected evaluation") }, { GdFrameSourceSelf.Present })
        return if (debugNode) GdNodeMembersXValue.child(descriptor, executor, frame, FACTORY)
        else GdSceneTreeNodeXValue(descriptor, executor, frame, FACTORY)
    }

    private fun nodeValue(
        executor: DapSessionExecutor,
        row: NodeRow,
        evaluation: suspend () -> DapEvaluationResult = { error("Unexpected evaluation") },
    ): XNamedValue {
        val descriptor = GdNodeDescriptor(objectId = 42, nodeName = "Test", className = "Node")
        val frame = GdSelfCheckedFrame(TestFrame { evaluation() }, { GdFrameSourceSelf.Present })
        return when (row) {
            NodeRow.ROOT -> GdSceneTreeNodeXValue("Root", executor, frame, FACTORY)
            NodeRow.SCENE -> GdSceneTreeNodeXValue(descriptor, executor, frame, FACTORY)
            NodeRow.CHILD -> GdNodeMembersXValue.child(descriptor, executor, frame, FACTORY)
            NodeRow.PARENT -> GdNodeMembersXValue.parentOf(42, executor, frame, FACTORY)
        }
    }

    private fun withBridge(consumer: Consumer, body: suspend (ControlledExecutor, Bridge) -> Unit): Unit =
        withExecutor { body(it, Bridge(consumer, it)) }

    private fun withExecutor(body: suspend (ControlledExecutor) -> Unit): Unit = withControlledTime {
        supervisorScope {
            val executor = ControlledExecutor(this)
            try {
                body(executor)
            }
            finally {
                executor.jobs.forEach { it.cancel() }
            }
        }
    }

    private class Bridge(private val consumer: Consumer, private val executor: ControlledExecutor) {
        val answers = mutableListOf<String>()
        private val success = XValueChildrenList.singleton(TestValue())
        val node = RecordingNode { children, last ->
            if (last) answers.add(if (children === success) "success" else "fallback")
        }
        private var promise: CancellablePromise<String>? = null

        fun submit(body: suspend (() -> Unit) -> Unit) {
            when (consumer) {
                Consumer.NODE -> executor.computeChildrenWithCompletion(node, LOG, "test children", errorMessage = { it.message!! }) { target ->
                    body { target.addChildren(success, true) }
                }
                Consumer.CALLBACK -> executor.evaluateWithCompletion(object : XEvaluationCallback {
                    override fun evaluated(result: XValue) { answers.add("success") }
                    override fun errorOccurred(errorMessage: String) { answers.add("fallback") }
                }) { answer -> body { answer.evaluated(TestValue()) } }
                Consumer.PROMISE -> {
                    val result = executor.computePromiseWithCompletion<String> { answer -> body { answer.setResult("success") } }
                    result.onSuccess { answers.add(it) }
                    result.onError { answers.add("fallback") }
                    promise = result as CancellablePromise<String>
                }
            }
        }

        fun assertFallback(cancelled: Boolean = false) {
            assertEquals(listOf("fallback"), answers)
            when (consumer) {
                Consumer.NODE -> {
                    assertEquals(1, node.terminalCount)
                    if (cancelled) assertTrue(node.errors.isEmpty())
                }
                Consumer.CALLBACK -> Unit
                Consumer.PROMISE -> assertEquals(cancelled, promise!!.isCancelled)
            }
        }
    }

    private class ControlledExecutor(private val scope: CoroutineScope) : DapSessionExecutor {
        var stopped = false
        var quiesced = false
        val jobs = mutableListOf<Deferred<*>>()
        val last: Deferred<*> get() = jobs.last()

        override fun post(block: suspend DapSessionContext.() -> Unit) {
            if (stopped || quiesced) return
            postAsync(block)
        }

        override fun <T> postAsync(block: suspend DapSessionContext.() -> T): Deferred<T> {
            if (stopped) throw stoppedSession()
            return scope.async(start = CoroutineStart.LAZY) { unusedSessionContext().block() }.also(jobs::add)
        }

        suspend fun runLast() {
            last.start()
            last.join()
        }
    }

    private class RecordingCallback : XEvaluationCallback {
        val values = mutableListOf<XValue>()
        val errors = mutableListOf<String>()
        val answerCount get() = values.size + errors.size
        override fun evaluated(result: XValue) { values.add(result) }
        override fun errorOccurred(errorMessage: String) { errors.add(errorMessage) }
    }

    private class RecordingNode(private val onBatch: (XValueChildrenList, Boolean) -> Unit = { _, _ -> }) : XCompositeNode {
        val batches = mutableListOf<Pair<XValueChildrenList, Boolean>>()
        val errors = mutableListOf<String>()
        val messages = mutableListOf<String>()
        var obsolete = false
        val terminalCount get() = batches.count { it.second }
        val values get() = batches.flatMap { (children, _) ->
            children.topValues + (0 until children.size()).map { children.getValue(it) as XNamedValue }
        }

        override fun addChildren(children: XValueChildrenList, last: Boolean) {
            batches.add(children to last)
            onBatch(children, last)
        }
        override fun setAlreadySorted(alreadySorted: Boolean) = Unit
        override fun setErrorMessage(errorMessage: String) { errors.add(errorMessage) }
        override fun setErrorMessage(errorMessage: String, link: XDebuggerTreeNodeHyperlink?) { errors.add(errorMessage) }
        override fun setMessage(message: String, icon: Icon?, attributes: SimpleTextAttributes, link: XDebuggerTreeNodeHyperlink?) {
            messages.add(message)
        }
        override fun tooManyChildren(remaining: Int) = Unit
        override fun isObsolete() = obsolete
    }

    private class RecordingValueNode : XValueNode {
        var presentations = 0
        val messages = mutableListOf<String>()
        val expandable = mutableListOf<Boolean>()
        val rendered = mutableListOf<String>()
        var obsolete = false

        override fun setPresentation(icon: Icon?, type: String?, value: String, hasChildren: Boolean) {
            presentations++
            messages.add(value)
            rendered.add(value)
            expandable.add(hasChildren)
        }
        override fun setPresentation(icon: Icon?, presentation: XValuePresentation, hasChildren: Boolean) {
            presentations++
            val text = StringBuilder()
            presentation.renderValue(object : XValuePresentation.XValueTextRenderer {
                override fun renderValue(value: String) { text.append(value) }
                override fun renderStringValue(value: String) { text.append(value) }
                override fun renderNumericValue(value: String) { text.append(value) }
                override fun renderKeywordValue(value: String) { text.append(value) }
                override fun renderValue(value: String, key: TextAttributesKey) { text.append(value) }
                override fun renderStringValue(value: String, additionalSpecialCharsToHighlight: String?, maxLength: Int) { text.append(value) }
                override fun renderComment(comment: String) { text.append(comment) }
                override fun renderSpecialSymbol(symbol: String) { text.append(symbol) }
                override fun renderError(error: String) { text.append(error) }
            })
            rendered.add(text.toString())
            expandable.add(hasChildren)
        }
        override fun setFullValueEvaluator(fullValueEvaluator: XFullValueEvaluator) = Unit
        override fun isObsolete() = obsolete
    }

    private class TestValue : XNamedValue("success") {
        override fun computePresentation(node: XValueNode, place: XValuePlace) = Unit
    }

    private class TestThread : DapThread {
        override val id = ThreadId(1)
        override val name = "thread"
        override var state: DapThreadState = DapThreadState.Paused(null)
        override val topFrame: DapStackFrame? = null
        override suspend fun DapSessionContext.topFrame(): DapStackFrame? = throw UnsupportedOperationException()
        override suspend fun DapSessionContext.stackFrames(): List<DapStackFrame> = throw UnsupportedOperationException()
        override suspend fun DapSessionContext.stackFrames(startFrame: Int, levels: Int): DapStackFramesChunk =
            throw UnsupportedOperationException()
        override fun stepInto(stepSize: StepSize, targetId: StepInTargetId?) = throw UnsupportedOperationException()
        override fun stepOver(stepSize: StepSize) = throw UnsupportedOperationException()
        override fun stepOut(stepSize: StepSize) = throw UnsupportedOperationException()
        override fun stepBack(stepSize: StepSize) = throw UnsupportedOperationException()
        override fun pause() = throw UnsupportedOperationException()
        override fun resume() = throw UnsupportedOperationException()
        override fun runToPosition(position: SourcePosition) = throw UnsupportedOperationException()
    }

    private class TestFrame(
        private val onScopes: suspend () -> Unit = {},
        private val evaluation: suspend (String) -> DapEvaluationResult,
    ) : DapStackFrame {
        var scopeName = "Members"
        var scopeNames: List<String>? = null
        var localVariables = listOf(variable("above", "int", "7"))
        var memberVariables = listOf(variable("self", "Object"))
        override val id = FrameId(1)
        override val name = "frame"
        override val type = StackFrameType.Normal
        override var source: VirtualFile? = null
        override val startPosition = TextPosition(1, 1)
        override val endPosition = TextPosition(1, 1)
        override val thread = TestThread()
        override suspend fun DapSessionContext.scopes(): List<DapScope> {
            onScopes()
            return (scopeNames ?: listOf(scopeName)).map { scopeName ->
                testScope(scopeName, this@TestFrame) {
                    if (scopeName == "Locals") localVariables else memberVariables
                }
            }
        }
        override suspend fun DapSessionContext.evaluate(expression: String): DapEvaluationResult = evaluation(expression)
    }

    companion object {
        private val LOG = Logger.getInstance(GdCompletionBridgeTest::class.java)
        private val FACTORY = DefaultDapXDebuggerPresentationFactory(null)
        private val projectFixture = projectFixture()

        private fun testScope(
            name: String = "Locals",
            frame: DapStackFrame = TestFrame { error("Unexpected evaluation") },
            expensive: Boolean = false,
            read: suspend () -> List<DapVariable>,
        ): DapScope = object : DapScope {
            override val frame = frame
            override val name = name
            override val isExpensive = expensive
            override suspend fun DapSessionContext.variables(): List<DapVariable> = read()
        }

        // The fake executor needs this exception without a platform session. The constructor is internal.
        private fun stoppedSession(): DapSessionStoppedException =
            DapSessionStoppedException::class.java.getDeclaredConstructor().apply { isAccessible = true }.newInstance()

        private fun connectionClosed(): DapConnectionClosedException =
            DapConnectionClosedException::class.java.getDeclaredConstructor(String::class.java)
                .apply { isAccessible = true }.newInstance("The connection closed")

        private fun numberedAnswer(expression: String, fields: List<DapVariable>): DapEvaluationResult {
            val inner = structuredVariable("0", "Array", listOf(variable("size", "int", fields.size.toString())) + fields)
            return numberedSuccessAnswer(expression, inner)
        }

        private fun probeFields(text: String, type: String, valid: Boolean = true, start: Int = 0): List<DapVariable> =
            listOf("String" to text, "bool" to valid.toString(), "String" to "<null>", "StringName" to "", "StringName" to type)
                .mapIndexed { index, (fieldType, value) -> variable((start + index).toString(), fieldType, value) }

        private fun structuredVariable(
            name: String = "result", type: String = "Object", children: List<DapVariable>? = null
        ): DapStructuredVariable = object : DapStructuredVariable, DapVariable by variable(name, type) {
            override val namedVariables = children?.size ?: 1
            override val indexedVariables = 0
            override fun DapSessionContext.loadVariables(filter: VariableFilter?, batchSize: Int): Flow<VariableBatch> =
                flowOf(VariableBatch(children ?: error("Unexpected variable request"), 0))
        }

        private fun variable(name: String, type: String, value: String = "value"): DapVariable = object : DapVariable {
            override val name = name
            override val type = type
            override val value = value
            override val evaluateName: String? = null
            override val kind: ValueKind? = null
            override val attributes: List<VariableAttribute>? = null
        }
    }
}
