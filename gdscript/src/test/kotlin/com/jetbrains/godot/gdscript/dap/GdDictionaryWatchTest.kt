package com.jetbrains.godot.gdscript.dap

import com.intellij.platform.dap.DapEvaluationResult
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.DapStackFrame
import com.intellij.platform.dap.DapStructuredVariable
import com.intellij.platform.dap.DapVariable
import com.intellij.platform.dap.FrameId
import com.intellij.platform.dap.VariableBatch
import com.intellij.platform.dap.VariableFilter
import com.intellij.platform.dap.xdebugger.DefaultDapXDebuggerPresentationFactory
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.RegistryKey
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.dap.protocol.DapRequestFailedException
import com.jetbrains.dap.protocol.Message
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XExpression
import com.intellij.xdebugger.frame.XNamedValue
import com.intellij.xdebugger.frame.XSuspendContext
import gdscript.dap.remote.GdDapXScope
import gdscript.dap.remote.GdDapXValue
import gdscript.dap.remote.GdDictionaryKeys
import gdscript.dap.remote.GdFrameSourceSelf
import gdscript.dap.remote.GdSelfCheckedFrame
import gdscript.dap.remote.GdWatchContext
import gdscript.dap.remote.GdWatchPath
import gdscript.dap.remote.GdWatchStop
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.job
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import org.jetbrains.concurrency.Promise
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.lang.reflect.Proxy
import java.util.concurrent.TimeUnit

@TestApplication
class GdDictionaryWatchTest {
    enum class ReadStage { EVALUATION, OUTER_VARIABLES, KEY_VARIABLES }

    @Test
    fun testCollidingLabelsUseTheOriginalResponsePositions(): Unit = withExecutor { executor ->
        val entries = listOf(variable("1", "String"), variable("1", "String"), variable("0", "String"))
        val paths = dictionaryPaths(entries)
        val requests = mutableListOf<String>()
        val frame = checked { expression ->
            requests.add(expression)
            keysAnswer(expression, listOf(variable("0", "int", "1"), variable("1", "String", "1"), variable("2", "int", "0")))
        }
        // The selected row keeps its original position even if the tree sorts the labels.
        val selectedInTreeOrder = listOf(paths[2], paths[0], paths[1])
        assertEquals(listOf("(items)[0]", "(items)[1]", "(items)[\"1\"]"),
                     selectedInTreeOrder.map { execute(it, frame, executor).expression() })
        assertEquals(3, requests.distinct().size)
        assertTrue(requests.all { it.startsWith("[((items)).keys() + [typeof(") })
        assertTrue(selectedInTreeOrder.all { it.text == null })
    }

    @ParameterizedTest(name = "{0}: {1}")
    @MethodSource("keyCases")
    fun testLosslessKeyLiterals(type: String, value: String, gameType: Int, literal: String) {
        assertEquals(literal, GdDictionaryKeys.encode(variable("0", type, value), gameType, "00000000"))
    }

    @Test
    fun testEscapedStringKey(): Unit = withExecutor { executor ->
        val key = "quote\" slash\\ newline\n tab\t emoji😀"
        val path = dictionaryPaths(listOf(variable(key, "int"))).single()
        val frame = checked { keysAnswer(it, listOf(variable("0", "String", key))) }
        assertEquals("(items)[\"quote\\\" slash\\\\ newline\\u000a tab\\u0009 emoji😀\"]",
                     execute(path, frame, executor).expression())
    }

    @ParameterizedTest
    @ValueSource(strings = ["float", "Vector2", "Rect2", "Basis", "Color", "Dictionary", "Array", "RID", "Callable",
        "Signal", "Unknown"])
    fun testUnknownOrLossyKeyRepresentationsGiveNoWatch(type: String): Unit = withExecutor { executor ->
        val path = dictionaryPaths(listOf(variable("representation", "int"))).single()
        val frame = checked { keysAnswer(it, listOf(variable("0", type, "representation"))) }
        execute(path, frame, executor).assertFailure("unreadable dictionary key type")
    }

    @ParameterizedTest
    @MethodSource("unreadableKeys")
    fun testMalformedKeyLiteralsGiveNoWatch(type: String, value: String, gameType: Int) {
        assertNull(GdDictionaryKeys.encode(variable("0", type, value), gameType, "00000000"))
    }

    @Test
    fun testPropertiesAndNestedDictionariesResolveFromTheRoot(): Unit = withExecutor { executor ->
        val outer = dictionaryPaths(listOf(variable("outer", "Object"))).single()
        val property = outer.properties("Members", listOf(variable("inner", "Dictionary"))).single()
        val entry = property.children(variable("inner", "Dictionary"), listOf(variable("1", "Object"))).single()
        val category = entry.category()
        val hp = category.properties("Members", listOf(variable("hp", "int"))).single()
        val requests = mutableListOf<String>()
        val frame = checked { expression ->
            requests.add(expression)
            val key = if (requests.size == 1) variable("0", "String", "outer") else variable("0", "int", "1")
            keysAnswer(expression, listOf(key))
        }
        assertEquals("(items)[\"outer\"].inner[1].hp", execute(hp, frame, executor).expression())
        assertEquals(2, requests.size)
        assertKeyRead(requests[0], "(items)", 0)
        assertKeyRead(requests[1], "(items)[\"outer\"].inner", 0)
        assertFalse(requests.any { ".values()" in it })
        val jobsBefore = executor.jobs.size
        execute(category, frame, executor).assertFailure("category is a group")
        assertEquals(jobsBefore, executor.jobs.size)
    }

    @ParameterizedTest
    @MethodSource("noUsableType")
    fun testMathShapedLabelsStayDictionaryKeysWithoutExpansionReads(type: String?): Unit = withExecutor { executor ->
        val entries = listOf("x", "y", "w", "h").map { variable(it, "float") }
        val parent = object : DapStructuredVariable by structuredVariable("items", "Dictionary", entries) {
            override val type = type
            override val value = "{ x: 1, y: 2, w: 3, h: 4 }"
        }
        var evaluations = 0
        val frame = checked {
            evaluations++
            keysAnswer(it, listOf(variable("0", "String", "x"), variable("1", "String", "y"),
                                  variable("2", "String", "w"), variable("3", "String", "h")))
        }
        val root = value(parent, GdWatchPath.evaluated("items"), frame, executor)
        val children = expand(root, executor)
        assertEquals(0, evaluations)
        assertEquals(listOf("x", "y", "w", "h"), children.values.map { it.name })
        assertEquals(listOf("(items)[\"x\"]", "(items)[\"y\"]", "(items)[\"w\"]", "(items)[\"h\"]"),
                     children.values.map { watch(it, executor) })
        assertEquals(4, evaluations)
        assertEquals(1, children.terminalCount)
        assertTrue(children.errors.isEmpty())
    }

    @ParameterizedTest
    @ValueSource(strings = ["Locals", "Members", "Globals"])
    fun testScopesKeepReadSafetySeparateFromRecovery(scope: String): Unit = withExecutor { executor ->
        val dictionary = structuredVariable("items", "Dictionary", listOf(variable("key", "String")))
        val rawFrame = TestFrame { keysAnswer(it, listOf(variable("0", "String", "key"))) }
        val frame = GdSelfCheckedFrame(rawFrame, { GdFrameSourceSelf.Present })
        val rows = RecordingNode()
        GdDapXScope(FACTORY, executor, testScope(scope, rawFrame) { listOf(dictionary) }, 0, watchFrame = frame).computeChildren(rows)
        executor.last.await()
        val children = expand(rows.values.single(), executor)
        assertEquals(if (scope == "Members") "self.items[\"key\"]" else "items[\"key\"]", watch(children.values.single(), executor))
    }

    @Test
    fun testMathAndArrayPartsBelowAnEntryKeepTheirSuffixes(): Unit = withExecutor { executor ->
        val path = dictionaryPaths(listOf(variable("key", "Array"))).single()
        val item = path.children(variable("key", "Array"), listOf(variable("0", "Basis"))).single()
        val axis = item.children(variable("0", "Basis"), listOf(variable("x", "Vector3"))).single()
        val component = axis.children(variable("x", "Vector3"), listOf(variable("y", "float"))).single()
        val frame = checked { keysAnswer(it, listOf(variable("0", "String", "key"))) }
        assertEquals("(items)[\"key\"][0].transposed().x.y", execute(component, frame, executor).expression())
    }

    @Test
    fun testChangedCountGivesNoWatch(): Unit = withExecutor { executor ->
        val frame = checked { keysAnswer(it, listOf(variable("0", "String", "key"), variable("1", "String", "extra"))) }
        execute(singlePath(), frame, executor).assertFailure("count changed or malformed")
    }

    @Test
    fun testChangedLabelGivesNoWatch(): Unit = withExecutor { executor ->
        val frame = checked { keysAnswer(it, listOf(variable("0", "String", "other"))) }
        execute(singlePath(), frame, executor).assertFailure("key labels changed")
    }

    @Test
    fun testChangedOrderGivesNoWatch(): Unit = withExecutor { executor ->
        val path = dictionaryPaths(listOf(variable("a", "int"), variable("b", "int"))).first()
        val frame = checked { keysAnswer(it, listOf(variable("0", "String", "b"), variable("1", "String", "a"))) }
        execute(path, frame, executor).assertFailure("key labels changed")
    }

    @Test
    fun testRejectedEvaluationGivesNoWatch(): Unit = withExecutor { executor ->
        execute(singlePath(), checked { DapEvaluationResult.Error("rejected") }, executor)
            .assertFailure("key evaluation rejected: rejected")
    }

    @Test
    fun testThrownRequestFailureGivesNoWatch(): Unit = withExecutor { executor ->
        execute(singlePath(), checked { throw IllegalStateException("request failed") }, executor).assertFailure("request failed")
    }

    @ParameterizedTest
    @EnumSource(ReadStage::class)
    fun testAdapterErrorsMarkedForTheUserStayInsideThePostedBlock(stage: ReadStage): Unit = withExecutor { executor ->
        val failure = DapRequestFailedException("adapter request failed", Message(format = "adapter error", showUser = true))
        fun failingVariable(name: String): DapStructuredVariable = object : DapStructuredVariable by structuredVariable(name, "Array") {
            override fun DapSessionContext.loadVariables(filter: VariableFilter?, batchSize: Int) =
                flow<VariableBatch> { throw failure }
        }
        val frame = checked { expression ->
            when (stage) {
                ReadStage.EVALUATION -> throw failure
                ReadStage.OUTER_VARIABLES -> DapEvaluationResult.Success(failingVariable("result"))
                ReadStage.KEY_VARIABLES -> numberedSuccessAnswer(expression, failingVariable("0"))
            }
        }
        execute(singlePath(), frame, executor).assertFailure("DapRequestFailedException: adapter request failed")
    }

    @Test
    fun testFailedParentKeyStopsNestedResolution(): Unit = withExecutor { executor ->
        val outer = dictionaryPaths(listOf(variable("key", "Dictionary"))).single()
        val nested = outer.children(variable("key", "Dictionary"), listOf(variable("inner", "int"))).single()
        var requests = 0
        val frame = checked { expression ->
            requests++
            keysAnswer(expression, listOf(variable("0", "Object", "key")))
        }
        execute(nested, frame, executor).assertFailure("dictionary key labels changed")
        assertEquals(1, requests)
    }

    @ParameterizedTest
    @EnumSource(value = GdFrameSourceSelf::class, names = ["Absent", "Unavailable"])
    internal fun testUnsafeFramesRejectOnlyPendingReads(source: GdFrameSourceSelf): Unit = withExecutor { executor ->
        val frame = GdSelfCheckedFrame(TestFrame { error("An unsafe frame must not evaluate") }, { source })
        execute(singlePath(), frame, executor).assertFailure("key evaluation rejected")
        assertEquals("items", execute(GdWatchPath.evaluated("items"), frame, executor).expression())
    }

    @Test
    fun testRunningFrameRejectsOnlyPendingReads(): Unit = timeoutRunBlocking {
        supervisorScope {
            val pause = CoroutineScope(coroutineContext + SupervisorJob(coroutineContext.job))
            val executor = ControlledExecutor(pause)
            var sourceChecks = 0
            val raw = TestFrame { error("A running frame must not evaluate") }
            val frame = GdSelfCheckedFrame(raw, {
                sourceChecks++
                GdFrameSourceSelf.Present
            })
            pause.cancel("IDE resume")
            execute(singlePath(), frame, executor).assertFailure("IDE resume", cancelled = true)
            assertEquals("items", execute(GdWatchPath.evaluated("items"), frame, executor).expression())
            assertEquals(0, sourceChecks)
        }
    }

    @Test
    fun testKnownPathsNeedNoFrameOrStopCheck(): Unit = withExecutor { executor ->
        val raw = TestFrame { error("A known path must not evaluate") }
        val frame = object : DapStackFrame by raw {
            override val thread get() = error("A known path must not check the thread")
            override val id get() = error("A known path must not check the frame")
        }
        executor.stopped = true
        assertEquals("items", execute(GdWatchPath.evaluated("items"), frame, executor).expression())
        assertTrue(executor.jobs.isEmpty())
    }

    @Test
    fun testReadUsesTheRowsFrame(): Unit = withExecutor { executor ->
        val requestedFrames = mutableListOf<FrameId>()
        val raw = TestFrame { error("The delegate must not evaluate") }
        val rowFrame = object : DapStackFrame by raw {
            override val id = FrameId(7)
            override suspend fun DapSessionContext.evaluate(expression: String): DapEvaluationResult {
                requestedFrames.add(id)
                return keysAnswer(expression, listOf(variable("0", "String", "key")))
            }
        }
        val checked = GdSelfCheckedFrame(rowFrame, { GdFrameSourceSelf.Present })
        assertEquals("(items)[\"key\"]", execute(singlePath(), checked, executor).expression())
        assertEquals(listOf(FrameId(7)), requestedFrames)
    }

    @ParameterizedTest
    @MethodSource("unreadableAnswers")
    fun testUnreadableAnswersGiveNoWatch(answer: DapVariable): Unit = withExecutor { executor ->
        val frame = checked { expression ->
            if (answer.name == "result") DapEvaluationResult.Success(answer)
            else DapEvaluationResult.Success(structuredVariable(children = listOf(
                variable("size", "int", "2"), answer, variable("1", "int", requestNumber(expression)),
            )))
        }
        execute(singlePath(), frame, executor).assertFailure()
    }

    @Test
    fun testKeyArrayRejectsNoncanonicalIndices(): Unit = withExecutor { executor ->
        val frame = checked { expression ->
            val fields = listOf(variable("00", "String", "key"), variable("1", "int", "4"), variable("2", "String", "00000000"))
            numberedAnswer(expression, fields)
        }
        execute(singlePath(), frame, executor).assertFailure("key array indices are malformed")
    }

    @Test
    fun testKeyVariablesFailureGivesNoWatch(): Unit = withExecutor { executor ->
        val frame = checked { expression ->
            numberedSuccessAnswer(expression, structuredVariable("0", "Array"))
        }
        execute(singlePath(), frame, executor).assertFailure("Unexpected variable request")
    }

    @Test
    @RegistryKey(key = "dap.timeout.evaluate", value = "1")
    fun testEvaluationTimeoutGivesNoWatch(): Unit = withControlledTime {
        supervisorScope {
            val executor = ControlledExecutor(this)
            execute(singlePath(), checked { awaitCancellation() }, executor).assertFailure("timed out")
        }
    }

    @ParameterizedTest
    @EnumSource(value = ReadStage::class, names = ["OUTER_VARIABLES", "KEY_VARIABLES"])
    @RegistryKey(key = "dap.timeout.evaluate", value = "1")
    fun testVariablesTimeoutGivesNoWatch(stage: ReadStage): Unit = withControlledTime {
        supervisorScope {
            val executor = ControlledExecutor(this)
            val frame = checked { expression ->
                val waiting = object : DapStructuredVariable by structuredVariable("0", "Array") {
                    override fun DapSessionContext.loadVariables(filter: VariableFilter?, batchSize: Int) =
                        flow<VariableBatch> { awaitCancellation() }
                }
                when (stage) {
                    ReadStage.OUTER_VARIABLES -> DapEvaluationResult.Success(waiting)
                    ReadStage.KEY_VARIABLES -> numberedSuccessAnswer(expression, waiting)
                    ReadStage.EVALUATION -> error("Expected a variable read")
                }
            }
            execute(singlePath(), frame, executor).assertFailure("timed out")
        }
    }

    @ParameterizedTest
    @EnumSource(value = ReadStage::class, names = ["OUTER_VARIABLES", "KEY_VARIABLES"])
    fun testPauseCancellationDuringVariablesCompletesOnce(stage: ReadStage): Unit = timeoutRunBlocking {
        supervisorScope {
            val pause = CoroutineScope(coroutineContext + SupervisorJob(coroutineContext.job))
            val executor = ControlledExecutor(pause)
            try {
                val entered = CompletableDeferred<Unit>()
                val waiting = object : DapStructuredVariable by structuredVariable("0", "Array") {
                    override fun DapSessionContext.loadVariables(filter: VariableFilter?, batchSize: Int) = flow<VariableBatch> {
                        entered.complete(Unit)
                        awaitCancellation()
                    }
                }
                val frame = checked { expression ->
                    when (stage) {
                        ReadStage.OUTER_VARIABLES -> DapEvaluationResult.Success(waiting)
                        ReadStage.KEY_VARIABLES -> numberedSuccessAnswer(expression, waiting)
                        ReadStage.EVALUATION -> error("Expected a variable read")
                    }
                }
                val action = observe(singlePath(), frame, executor)
                executor.last.start()
                entered.await()
                pause.cancel("Godot editor resume")
                executor.last.join()
                action.assertFailure("Godot editor resume", cancelled = true)
            }
            finally {
                pause.cancel()
            }
        }
    }

    @Test
    fun testStaleRowAfterClearingPauseChildrenPostsNoKeyRead(): Unit = timeoutRunBlocking {
        supervisorScope {
            val pause = CoroutineScope(coroutineContext + SupervisorJob(coroutineContext.job))
            val executor = ControlledExecutor(pause)
            try {
                val session = WatchSession(executor)
                val entered = CompletableDeferred<Unit>()
                val pending = executor.postAsync {
                    entered.complete(Unit)
                    awaitCancellation()
                }
                pending.start()
                entered.await()
                session.current = null
                // The IDE clears current children. It keeps the pause executor usable.
                pause.coroutineContext.cancelChildren()
                pending.join()
                assertTrue(pending.isCancelled)
                assertTrue(pause.coroutineContext.job.isActive)
                assertEquals(42, executor.postAsync { 42 }.await())
                val frame = checked(session.stop) { error("A stale row must not evaluate") }
                execute(singlePath(), frame, executor).assertFailure("stop is no longer current")
                // A later stop cannot make the old row current, even with the same thread and frame IDs.
                session.current = session.nextContext(executor)
                execute(singlePath(), frame, executor).assertFailure("stop is no longer current")
                assertEquals("items", execute(GdWatchPath.evaluated("items"), frame, executor).expression())
            }
            finally {
                pause.cancel()
            }
        }
    }

    @ParameterizedTest
    @EnumSource(ReadStage::class)
    fun testIdeStepRejectsAnInFlightAnswerAndKeepsTheExecutorUsable(stage: ReadStage): Unit = timeoutRunBlocking {
        supervisorScope {
            val pause = CoroutineScope(coroutineContext + SupervisorJob(coroutineContext.job))
            val executor = ControlledExecutor(pause)
            try {
                val session = WatchSession(executor)
                val entered = CompletableDeferred<Unit>()
                val release = CompletableDeferred<Unit>()
                suspend fun waitForStep() = withContext(NonCancellable) {
                    entered.complete(Unit)
                    release.await()
                }
                val frame = delayedKeysFrame(stage, session.stop, wait = ::waitForStep)
                val action = observe(singlePath(), frame, executor)
                executor.last.start()
                entered.await()
                session.current = null
                pause.coroutineContext.cancelChildren()
                release.complete(Unit)
                executor.last.join()
                action.assertFailure("cancel", cancelled = true)
                assertTrue(pause.coroutineContext.job.isActive)
                assertEquals(42, executor.postAsync { 42 }.await())
                execute(singlePath(), frame, executor).assertFailure("stop is no longer current")
                action.assertFailure("cancel", cancelled = true)
            }
            finally {
                pause.cancel()
            }
        }
    }

    @ParameterizedTest
    @EnumSource(ReadStage::class)
    fun testStopChangeDuringAReadRejectsTheAnswerWithoutCancellation(stage: ReadStage): Unit = withExecutor { executor ->
        val session = WatchSession(executor)
        val entered = CompletableDeferred<Unit>()
        val release = CompletableDeferred<Unit>()
        val frame = delayedKeysFrame(stage, session.stop) {
            entered.complete(Unit)
            release.await()
        }
        val action = observe(singlePath(), frame, executor)
        executor.last.start()
        entered.await()
        session.current = session.nextContext(executor)
        release.complete(Unit)
        executor.last.join()
        action.assertFailure("stop is no longer current")
    }

    @Test
    fun testResumeBeforePromiseCompletionRejectsTheReadResult(): Unit = withExecutor { executor ->
        val session = WatchSession(executor)
        var checks = 0
        session.onCheck = {
            checks++
            if (checks == 3) session.current = null
        }
        val frame = checked(session.stop) { keysAnswer(it, listOf(variable("0", "String", "key"))) }
        execute(singlePath(), frame, executor).assertFailure("stop is no longer current")
        assertEquals(3, checks)
    }

    @Test
    fun testUnsuspendedSessionRejectsAReadWithTheSameContext(): Unit = withExecutor { executor ->
        val session = WatchSession(executor)
        session.suspended = false
        val frame = checked(session.stop) { error("An unsuspended session must not evaluate") }
        execute(singlePath(), frame, executor).assertFailure("stop is no longer current")
    }

    @Test
    fun testKnownPathsDoNotReadTheStopBinding(): Unit = withExecutor { executor ->
        val stop = GdWatchStop({ error("A known path must not check the stop") }, { emptyArray() })
        val frame = checked(stop) { error("A known path must not evaluate") }
        assertEquals("items", execute(GdWatchPath.evaluated("items"), frame, executor).expression())
        assertTrue(executor.jobs.isEmpty())
    }

    @Test
    fun testCancellationBeforeTheReadStartsCompletesOnce(): Unit = withExecutor { executor ->
        val action = observe(singlePath(), checked { error("Cancelled work must not evaluate") }, executor)
        executor.last.cancel()
        executor.last.join()
        action.assertFailure("cancel", cancelled = true)
    }

    @Test
    fun testStoppedSessionCompletesOnceWithoutARead(): Unit = withExecutor { executor ->
        executor.stopped = true
        execute(singlePath(), checked { error("A stopped session must not evaluate") }, executor).assertFailure("session stopped")
        assertTrue(executor.jobs.isEmpty())
    }

    @Test
    fun testEarlierStopAnswerCannotMatchANewStopRequest(): Unit = timeoutRunBlocking {
        supervisorScope {
            val firstPause = CoroutineScope(coroutineContext + SupervisorJob(coroutineContext.job))
            val earlier = CompletableDeferred<DapEvaluationResult>()
            val firstFrame = checked { expression ->
                keysAnswer(expression, listOf(variable("0", "String", "key"))).also { earlier.complete(it) }
            }
            assertEquals("(items)[\"key\"]", execute(singlePath(), firstFrame, ControlledExecutor(firstPause)).expression())
            firstPause.cancel("resume")
            val nextFrame = checked { earlier.await() }
            execute(singlePath(), nextFrame, ControlledExecutor(this)).assertFailure("different request number")
        }
    }

    @Test
    fun testIdenticalLabelsSelectTheCurrentRealKeyAfterAnUnobservedEditorStop(): Unit = timeoutRunBlocking {
        supervisorScope {
            val pause = CoroutineScope(coroutineContext + SupervisorJob(coroutineContext.job))
            val executor = ControlledExecutor(pause)
            try {
                val entered = CompletableDeferred<String>()
                val release = CompletableDeferred<Unit>()
                var key = variable("0", "int", "1")
                val frame = checked { expression ->
                    entered.complete(expression)
                    release.await()
                    keysAnswer(expression, listOf(key))
                }
                val path = dictionaryPaths(listOf(variable("1", "String"))).single()
                val action = observe(path, frame, executor)
                executor.last.start()
                entered.await()
                // An unobserved stop leaves the pause active. Equal labels cannot identify the replacement.
                key = variable("0", "String", "1")
                release.complete(Unit)
                executor.last.join()
                assertEquals("(items)[\"1\"]", action.expression())
                assertTrue(action.messages.isEmpty())
                // The observed editor transition cancels the old pause, including subsequent pending reads.
                pause.cancel("Godot editor resume and new stop")
                execute(path, frame, executor).assertFailure("Godot editor resume and new stop", cancelled = true)
                assertEquals("items", execute(GdWatchPath.evaluated("items"), frame, executor).expression())
            }
            finally {
                pause.cancel()
            }
        }
    }

    @Test
    fun testValueFlowKeepsPropertiesBelowNestedDictionaryEntries(): Unit = withExecutor { executor ->
        val innerEntry = structuredVariable("1", "Object", listOf(
            structuredVariable("Members", "Category", listOf(variable("hp", "int"))),
        ))
        val inner = structuredVariable("inner", "Dictionary", listOf(innerEntry))
        val outerEntry = structuredVariable("outer", "Object", listOf(structuredVariable("Members", "Category", listOf(inner))))
        val dictionary = structuredVariable("items", "Dictionary", listOf(outerEntry))
        val requests = mutableListOf<String>()
        val frame = checked { expression ->
            requests.add(expression)
            keysAnswer(expression, listOf(if (requests.size == 1) variable("0", "String", "outer") else variable("0", "int", "1")))
        }
        val root = value(dictionary, GdWatchPath.evaluated("items"), frame, executor)
        var selected: XNamedValue = root
        repeat(6) { selected = expand(selected, executor).values.single() }
        assertTrue(requests.isEmpty())
        assertEquals("(items)[\"outer\"].inner[1].hp", watch(selected, executor))
        assertEquals(2, requests.size)
    }

    @Test
    fun testConcurrentReadsMatchOnlyTheirOwnRequestNumber(): Unit = withExecutor { executor ->
        val firstEntered = CompletableDeferred<String>()
        val secondEntered = CompletableDeferred<String>()
        val firstAnswer = CompletableDeferred<DapEvaluationResult>()
        val secondAnswer = CompletableDeferred<DapEvaluationResult>()
        val firstFrame = checked {
            firstEntered.complete(it)
            firstAnswer.await()
        }
        val secondFrame = checked {
            secondEntered.complete(it)
            secondAnswer.await()
        }
        val first = observe(singlePath(), firstFrame, executor)
        val firstWork = executor.last
        val second = observe(singlePath(), secondFrame, executor)
        val secondWork = executor.last
        firstWork.start()
        secondWork.start()
        val firstExpression = firstEntered.await()
        val secondExpression = secondEntered.await()
        assertFalse(requestNumber(firstExpression) == requestNumber(secondExpression))
        // A swapped answer cannot create either watch, regardless of completion order.
        secondAnswer.complete(keysAnswer(firstExpression, listOf(variable("0", "String", "key"))))
        secondWork.join()
        firstAnswer.complete(keysAnswer(secondExpression, listOf(variable("0", "String", "key"))))
        firstWork.join()
        first.assertFailure("different request number")
        second.assertFailure("different request number")
    }

    @Test
    fun testSessionStopDuringEvaluationCompletesOnce(): Unit = withExecutor { executor ->
        execute(singlePath(), checked { throw stoppedSession() }, executor).assertFailure("DapSessionStoppedException")
    }

    @Test
    fun testUncheckedFrameGivesNoWatchWithoutARead(): Unit = withExecutor { executor ->
        execute(singlePath(), TestFrame { error("An unchecked frame must not evaluate") }, executor).assertFailure("no checked frame")
        assertTrue(executor.jobs.isEmpty())
    }

    @ParameterizedTest(name = "{0}: {1}")
    @MethodSource("objectIds")
    fun testLiveObjectKeysUseTheGameId(kind: String, id: Long, literal: String): Unit = withExecutor { executor ->
        val path = objectPath()
        var evaluations = 0
        var outerLoads = 0
        var keyLoads = 0
        val frame = checked { expression ->
            evaluations++
            assertKeyRead(expression, "(items)", 0)
            val key = objectKey(222)
            val fields = keyFields(expression, listOf(key), "24", objectData(id))
            val inner = object : DapStructuredVariable by structuredVariable("0", "Array",
                listOf(variable("size", "int", "3")) + fields) {
                override fun DapSessionContext.loadVariables(filter: VariableFilter?, batchSize: Int) = flow {
                    keyLoads++
                    emit(VariableBatch(listOf(variable("size", "int", "3")) + fields, 0))
                }
            }
            val outer = (numberedSuccessAnswer(expression, inner) as DapEvaluationResult.Success).variable as DapStructuredVariable
            DapEvaluationResult.Success(object : DapStructuredVariable by outer {
                override fun DapSessionContext.loadVariables(filter: VariableFilter?, batchSize: Int) = flow {
                    outerLoads++
                    outer.run { loadVariables(filter, batchSize) }.collect { emit(it) }
                }
            })
        }
        val action = execute(path, frame, executor)
        assertEquals("(items)[instance_from_id($literal)]", action.expression(), kind)
        assertTrue(action.messages.isEmpty())
        assertEquals(1, evaluations)
        assertEquals(1, outerLoads)
        assertEquals(1, keyLoads)
        assertEquals(1, executor.jobs.size)
    }

    @ParameterizedTest
    @ValueSource(strings = ["Object", "String"])
    fun testWrapperShapedDisplayLabelsAcceptTheCurrentObjectKey(displayType: String): Unit = withExecutor { executor ->
        val path = dictionaryPaths(listOf(variable("<EncodedObjectAsID#5>", displayType))).single()
        val frame = checked { keysAnswer(it, listOf(objectKey(99)), data = objectData(4000)) }
        assertEquals("(items)[instance_from_id(4000)]", execute(path, frame, executor).expression())
    }

    @Test
    fun testReorderedObjectKeysCaptureTheLaterId(): Unit = withExecutor { executor ->
        val path = dictionaryPaths(listOf(variable("<EncodedObjectAsID#11>", "int"),
                                          variable("<EncodedObjectAsID#12>", "int"))).first()
        val keys = listOf(objectKey(92), variable("1", "Object", "<EncodedObjectAsID#91>"))
        val frame = checked { keysAnswer(it, keys, data = objectData(200)) }
        assertEquals("(items)[instance_from_id(200)]", execute(path, frame, executor).expression())
    }

    @Test
    fun testGameThreadReorderCanSupplyALaterObjectId(): Unit = withExecutor { executor ->
        val path = dictionaryPaths(listOf(variable("<EncodedObjectAsID#11>", "int"),
                                          variable("<EncodedObjectAsID#12>", "int"))).first()
        val labelsRead = CompletableDeferred<Unit>()
        val resumeRead = CompletableDeferred<Unit>()
        var selectedId = 100L
        val frame = checked { expression ->
            val keys = listOf(objectKey(91), variable("1", "Object", "<EncodedObjectAsID#92>"))
            labelsRead.complete(Unit)
            resumeRead.await()
            keysAnswer(expression, keys, data = objectData(selectedId))
        }
        val action = observe(path, frame, executor)
        executor.last.start()
        labelsRead.await()
        selectedId = 200L
        resumeRead.complete(Unit)
        executor.last.join()
        assertEquals("(items)[instance_from_id(200)]", action.expression())
        assertTrue(action.messages.isEmpty())
    }

    @Test
    fun testMixedDictionaryChecksObjectLabelsForEverySelectedKey(): Unit = withExecutor { executor ->
        val paths = dictionaryPaths(listOf(variable("<EncodedObjectAsID#11>", "int"), variable("plain", "int")))
        var requests = 0
        val frame = checked { expression ->
            requests++
            keysAnswer(expression, listOf(objectKey(22), variable("1", "String", "plain")), data = objectData(4000))
        }
        assertEquals(listOf("(items)[instance_from_id(4000)]", "(items)[\"plain\"]"),
                     paths.map { execute(it, frame, executor).expression() })
        assertEquals(2, requests)
    }

    @ParameterizedTest
    @MethodSource("changedLabels")
    fun testLabelChecksFollowTheCurrentKeyType(displayed: List<String>, current: List<DapVariable>): Unit = withExecutor { executor ->
        val path = dictionaryPaths(displayed.map { variable(it, "int") }).last()
        val frame = checked { keysAnswer(it, current) }
        execute(path, frame, executor).assertFailure("dictionary key labels changed")
    }

    @Test
    fun testWrapperShapedStringKeyKeepsItsLiteral(): Unit = withExecutor { executor ->
        val key = "<EncodedObjectAsID#11>"
        val path = dictionaryPaths(listOf(variable(key, "int"))).single()
        val frame = checked { keysAnswer(it, listOf(variable("0", "String", key))) }
        assertEquals("(items)[\"$key\"]", execute(path, frame, executor).expression())
    }

    @Test
    fun testLargeStringKeyKeepsItsLiteral(): Unit = withExecutor { executor ->
        val key = "a".repeat(400000)
        val path = dictionaryPaths(listOf(variable(key, "int"))).single()
        val frame = checked {
            assertKeyRead(it, "(items)", 0)
            keysAnswer(it, listOf(variable("0", "String", key)), data = "04000000801a060061616161")
        }
        assertEquals("(items)[\"$key\"]", execute(path, frame, executor).expression())
    }

    @ParameterizedTest
    @MethodSource("invalidObjectData")
    fun testMalformedObjectDataCompletesWithOneReason(data: String): Unit = withExecutor { executor ->
        val frame = checked { keysAnswer(it, listOf(objectKey()), data = data) }
        execute(objectPath(), frame, executor).assertFailure("unreadable dictionary key type or data")
    }

    @ParameterizedTest
    @ValueSource(strings = ["type row type", "type row value", "noncanonical type", "wrong game type", "data row type"])
    fun testMalformedMetadataGivesNoWatch(defect: String): Unit = withExecutor { executor ->
        val frame = checked { expression ->
            val fields = keyFields(expression, listOf(objectKey()))
            val changed = when (defect) {
                "type row type" -> listOf(fields[0], variable("1", "String", "24"), fields[2])
                "type row value" -> listOf(fields[0], variable("1", "int", "Object"), fields[2])
                "noncanonical type" -> listOf(fields[0], variable("1", "int", "024"), fields[2])
                "wrong game type" -> listOf(fields[0], variable("1", "int", "2"), fields[2])
                "data row type" -> listOf(fields[0], fields[1], variable("2", "Object", fields[2].value))
                else -> error("Unknown metadata defect")
            }
            numberedAnswer(expression, changed)
        }
        execute(objectPath(), frame, executor).assertFailure()
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "zzzzzzzz", "0000000", "00000000000000000000000000"])
    fun testMalformedDataAlsoRejectsANonObjectKey(data: String): Unit = withExecutor { executor ->
        val frame = checked { keysAnswer(it, listOf(variable("0", "String", "key")), data = data) }
        execute(singlePath(), frame, executor).assertFailure("unreadable dictionary key type or data")
    }

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun testNilTransportDistinguishesANilKeyFromANullObject(nullObject: Boolean): Unit = withExecutor { executor ->
        val path = dictionaryPaths(listOf(variable("<null>", "int"))).single()
        val frame = checked {
            keysAnswer(it, listOf(variable("0", "Nil", "<null>")), gameType = if (nullObject) "24" else "0", data = "00000000")
        }
        val action = execute(path, frame, executor)
        if (nullObject) action.assertFailure("unreadable dictionary key type or data")
        else assertEquals("(items)[null]", action.expression())
    }

    @Test
    fun testNestedObjectKeysKeepTheParentPathAndSuffix(): Unit = withExecutor { executor ->
        val outer = objectPath()
        val inner = outer.properties("Members", listOf(variable("inner", "Dictionary"))).single()
        val entry = inner.children(variable("inner", "Dictionary"), listOf(variable("<EncodedObjectAsID#12>", "Object"))).single()
        val hp = entry.category().properties("Members", listOf(variable("hp", "int"))).single()
        val requests = mutableListOf<String>()
        val frame = checked { expression ->
            requests.add(expression)
            keysAnswer(expression, listOf(objectKey(999)), data = objectData(if (requests.size == 1) 4000 else -9000))
        }
        assertEquals("(items)[instance_from_id(4000)].inner[instance_from_id(-9000)].hp", execute(hp, frame, executor).expression())
        assertEquals(2, requests.size)
        assertKeyRead(requests[0], "(items)", 0)
        assertKeyRead(requests[1], "(items)[instance_from_id(4000)].inner", 0)
    }

    @Test
    fun testAChangeDuringTheReadKeepsTheCapturedNonObjectKey(): Unit = withExecutor { executor ->
        val frame = checked { keysAnswer(it, listOf(variable("0", "String", "key")), data = "04000000050000006f746865") }
        assertEquals("(items)[\"key\"]", execute(singlePath(), frame, executor).expression())
        val changedType = checked { keysAnswer(it, listOf(variable("0", "String", "key")), gameType = "2") }
        execute(singlePath(), changedType, executor).assertFailure("unreadable dictionary key type or data")
    }

    @Test
    fun testObjectKeyReadsMatchOnlyTheirOwnRequestNumber(): Unit = withExecutor { executor ->
        val firstEntered = CompletableDeferred<String>()
        val secondEntered = CompletableDeferred<String>()
        val firstAnswer = CompletableDeferred<DapEvaluationResult>()
        val secondAnswer = CompletableDeferred<DapEvaluationResult>()
        val first = observe(objectPath(), checked {
            firstEntered.complete(it)
            firstAnswer.await()
        }, executor)
        val firstWork = executor.last
        val second = observe(objectPath(), checked {
            secondEntered.complete(it)
            secondAnswer.await()
        }, executor)
        val secondWork = executor.last
        firstWork.start()
        secondWork.start()
        val firstRequest = firstEntered.await()
        val secondRequest = secondEntered.await()
        assertFalse(requestNumber(firstRequest) == requestNumber(secondRequest))
        secondAnswer.complete(keysAnswer(firstRequest, listOf(objectKey())))
        secondWork.join()
        firstAnswer.complete(keysAnswer(secondRequest, listOf(objectKey())))
        firstWork.join()
        first.assertFailure("different request number")
        second.assertFailure("different request number")
        execute(objectPath(), checked { keysAnswer(firstRequest, listOf(objectKey())) }, executor)
            .assertFailure("different request number")
    }

    private fun singlePath(): GdWatchPath = dictionaryPaths(listOf(variable("key", "String"))).single()

    private fun objectPath(): GdWatchPath = dictionaryPaths(listOf(variable("<EncodedObjectAsID#111>", "int"))).single()

    private fun objectKey(wrapper: Long = 222): DapStructuredVariable =
        object : DapStructuredVariable by structuredVariable("0", "Object") {
            override val value = "<EncodedObjectAsID#$wrapper>"
        }

    private fun objectData(id: Long): String = "18000100" + (0 until 8).joinToString("") {
        ((id ushr (it * 8)) and 255).toString(16).padStart(2, '0')
    }

    private fun assertKeyRead(expression: String, parent: String, position: Int) {
        val key = "($parent).keys()[$position]"
        assertEquals("[($parent).keys() + [typeof($key), var_to_bytes($key).slice(0, 12).hex_encode()], ${requestNumber(expression)}]",
                     expression)
    }

    private fun dictionaryPaths(entries: List<DapVariable>): List<GdWatchPath> =
        GdWatchPath.evaluated("items").children(variable("items", "Dictionary"), entries)

    private fun checked(stop: GdWatchStop? = null, evaluate: suspend (String) -> DapEvaluationResult): GdSelfCheckedFrame =
        GdSelfCheckedFrame(TestFrame(evaluation = evaluate), { GdFrameSourceSelf.Present }, watchStop = stop)

    private class WatchSession(executor: DapSessionExecutor) {
        private val thread = TestThread()
        val original: XSuspendContext = nextContext(executor)
        var current: XSuspendContext? = original
        var suspended = true
        var onCheck: () -> Unit = {}
        private val session = Proxy.newProxyInstance(XDebugSession::class.java.classLoader, arrayOf(XDebugSession::class.java)) {
            _, method, _ ->
            when (method.name) {
                "isSuspended" -> suspended && current != null
                "getSuspendContext" -> current
                else -> error("Unexpected session call: ${method.name}")
            }
        } as XDebugSession
        val stop = GdWatchStop({ original }, {
            onCheck()
            arrayOf(session)
        })

        fun nextContext(executor: DapSessionExecutor): XSuspendContext =
            FACTORY.createSuspendContext(executor, listOf(thread), thread)
    }

    private fun delayedKeysFrame(
        stage: ReadStage, stop: GdWatchStop, keys: List<DapVariable> = listOf(variable("0", "String", "key")), wait: suspend () -> Unit,
    ): GdSelfCheckedFrame = checked(stop) { expression ->
        when (stage) {
            ReadStage.EVALUATION -> wait()
            ReadStage.OUTER_VARIABLES, ReadStage.KEY_VARIABLES -> Unit
        }
        val answer = keysAnswer(expression, keys) as DapEvaluationResult.Success
        fun delayed(variable: DapStructuredVariable) = object : DapStructuredVariable by variable {
            override fun DapSessionContext.loadVariables(filter: VariableFilter?, batchSize: Int) = flow {
                wait()
                variable.run { loadVariables(filter, batchSize) }.collect { emit(it) }
            }
        }
        when (stage) {
            ReadStage.EVALUATION -> answer
            ReadStage.OUTER_VARIABLES -> DapEvaluationResult.Success(delayed(answer.variable as DapStructuredVariable))
            ReadStage.KEY_VARIABLES -> {
                val fields = keyFields(expression, keys)
                numberedSuccessAnswer(expression, delayed(structuredVariable("0", "Array",
                    listOf(variable("size", "int", fields.size.toString())) + fields)))
            }
        }
    }

    private fun keysAnswer(
        expression: String, keys: List<DapVariable>, gameType: String? = null, data: String? = null,
    ): DapEvaluationResult = numberedAnswer(expression, keyFields(expression, keys, gameType, data))

    private fun keyFields(
        expression: String, keys: List<DapVariable>, gameType: String? = null, data: String? = null,
    ): List<DapVariable> {
        val position = Regex("\\.keys\\(\\)\\[(\\d+)]").find(expression)!!.groupValues[1].toInt()
        val type = gameType ?: when (keys.getOrNull(position)?.type) {
            "Nil" -> "0"
            "bool" -> "1"
            "int" -> "2"
            "String" -> "4"
            "Vector2i" -> "6"
            "Vector3i" -> "10"
            "Vector4i" -> "13"
            "StringName" -> "21"
            "NodePath" -> "22"
            "Object" -> "24"
            else -> "-1"
        }
        val bytes = data ?: if (type == "24") objectData(4000) else "00000000"
        return keys + listOf(variable(keys.size.toString(), "int", type), variable((keys.size + 1).toString(), "String", bytes))
    }

    private fun requestNumber(expression: String): String = expression.substringAfterLast(", ").removeSuffix("]")

    private suspend fun execute(path: GdWatchPath, frame: DapStackFrame, executor: ControlledExecutor): Action {
        val jobsBefore = executor.jobs.size
        val action = observe(path, frame, executor)
        if (executor.jobs.size > jobsBefore) executor.runLast()
        return action
    }

    private fun observe(path: GdWatchPath, frame: DapStackFrame, executor: ControlledExecutor): Action {
        val jobsBefore = executor.jobs.size
        val action = Action(path.role)
        action.promise = GdWatchContext(path, frame, executor).calculateEvaluationExpression(action.messages::add)
        if (executor.jobs.size > jobsBefore) action.work = executor.last
        action.promise.onSuccess { action.completions++ }
        action.promise.onError { action.rejections++ }
        return action
    }

    private class Action(private val role: String) {
        lateinit var promise: Promise<XExpression>
        var work: Deferred<*>? = null
        val messages = mutableListOf<String>()
        var completions = 0
        var rejections = 0

        fun expression(): String? {
            assertEquals(Promise.State.SUCCEEDED, promise.state)
            assertEquals(1, completions)
            assertEquals(0, rejections)
            return promise.blockingGet(0, TimeUnit.SECONDS)?.expression
        }

        fun assertFailure(reason: String = "", cancelled: Boolean = false) {
            assertNull(expression())
            work?.let {
                assertTrue(it.isCompleted)
                assertEquals(cancelled, it.isCancelled, "Expected read failures must not escape the posted block")
            }
            assertEquals(1, messages.size)
            assertTrue(messages.single().contains(role), messages.single())
            assertTrue(messages.single().contains(reason, ignoreCase = true), messages.single())
        }
    }

    private fun value(
        variable: DapVariable, path: GdWatchPath, frame: GdSelfCheckedFrame, executor: DapSessionExecutor,
    ): GdDapXValue = GdDapXValue(FACTORY, executor, variable, owner = null, frame = null,
                                watchContext = GdWatchContext(path, frame, executor))

    private suspend fun expand(value: XNamedValue, executor: ControlledExecutor): RecordingNode {
        val rows = RecordingNode()
        value.computeChildren(rows)
        executor.last.await()
        return rows
    }

    private suspend fun watch(row: XNamedValue, executor: ControlledExecutor): String? {
        val before = executor.jobs.size
        val promise = row.calculateEvaluationExpression()
        if (executor.jobs.size > before) executor.runLast()
        assertEquals(Promise.State.SUCCEEDED, promise.state)
        return promise.blockingGet(0, TimeUnit.SECONDS)?.expression
    }

    private fun withExecutor(body: suspend (ControlledExecutor) -> Unit): Unit = timeoutRunBlocking {
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

    companion object {
        private val FACTORY = DefaultDapXDebuggerPresentationFactory(null)

        @JvmStatic
        fun noUsableType(): List<String?> = listOf(null, "Nil")

        @JvmStatic
        fun changedLabels(): List<Arguments> {
            val displayed = listOf("<EncodedObjectAsID#11>", "plain")
            val objectKey = variable("0", "Object", "<EncodedObjectAsID#22>")
            val plainKey = variable("1", "String", "plain")
            return listOf(
                Arguments.of(listOf("not a wrapper", "plain"), listOf(objectKey, plainKey)),
                Arguments.of(displayed, listOf(variable("0", "Object", "not a wrapper"), plainKey)),
                Arguments.of(displayed, listOf(objectKey, variable("1", "String", "changed"))),
                Arguments.of(displayed, listOf(variable("0", "String", "<EncodedObjectAsID#22>"), plainKey)),
            )
        }

        @JvmStatic
        fun unreadableAnswers(): List<DapVariable> = listOf(
            variable("result", "Nil", "<null>"), structuredVariable("0", "Object"), variable("0", "Array"),
        )

        @JvmStatic
        fun objectIds(): List<Arguments> = listOf(
            Arguments.of("Object", 4000L, "4000"),
            Arguments.of("Node", 72623859790382856L, "72623859790382856"),
            Arguments.of("RefCounted", -9223372036837998592L, "-9223372036837998592"),
            Arguments.of("Object", Long.MAX_VALUE, "9223372036854775807"),
            Arguments.of("Object", 1L, "1"),
            Arguments.of("RefCounted", -1L, "-1"),
            Arguments.of("RefCounted", Long.MIN_VALUE, "(-9223372036854775807 - 1)"),
        )

        @JvmStatic
        fun invalidObjectData(): List<String> = listOf(
            "", "00000000", "18000100", "1800010001000000000000", "18000100010000000000000000",
            "18000100010000000000000", "18000100010000000000000g", "18000100010000000000000 ",
            "1800010001000000000000+1", "1800010001000000000000١١", "180001000000000000000000",
            "190001000100000000000000", "180000000100000000000000", "180101000100000000000000",
            "180002000100000000000000", "180001010100000000000000", "180000010100000000000000",
        )

        @JvmStatic
        fun keyCases(): List<Arguments> = listOf(
            Arguments.of("int", "1", 2, "1"),
            Arguments.of("int", "-7", 2, "-7"),
            Arguments.of("int", "9223372036854775807", 2, "9223372036854775807"),
            Arguments.of("int", "-9223372036854775808", 2, "(-9223372036854775807 - 1)"),
            Arguments.of("bool", "true", 1, "true"),
            Arguments.of("bool", "false", 1, "false"),
            Arguments.of("Nil", "<null>", 0, "null"),
            Arguments.of("String", "1", 4, "\"1\""),
            Arguments.of("String", "", 4, "\"\""),
            Arguments.of("StringName", "player", 21, "StringName(\"player\")"),
            Arguments.of("NodePath", "../Player:position", 22, "NodePath(\"../Player:position\")"),
            Arguments.of("Vector2i", "(1, -2)", 6, "Vector2i(1, -2)"),
            Arguments.of("Vector3i", "(1, -2, 3)", 10, "Vector3i(1, -2, 3)"),
            Arguments.of("Vector4i", "(1, -2, 3, 4)", 13, "Vector4i(1, -2, 3, 4)"),
        )

        @JvmStatic
        fun unreadableKeys(): List<Arguments> = listOf(
            Arguments.of("int", "01", 2), Arguments.of("int", "+1", 2), Arguments.of("int", "9223372036854775808", 2),
            Arguments.of("bool", "True", 1), Arguments.of("Nil", "null", 0), Arguments.of("String", "\uD800", 4),
            Arguments.of("String", "\uDC00", 4), Arguments.of("String", "a\u0000b", 4), Arguments.of("Vector2i", "(1.0, 2)", 6),
            Arguments.of("Vector2i", "(2147483648, 2)", 6), Arguments.of("Vector3i", "(1, 2)", 10),
            Arguments.of("Vector4i", "Vector4i(1, 2, 3, 4)", 13),
        )
    }
}
