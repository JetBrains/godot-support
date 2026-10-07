package com.jetbrains.godot.gdscript.dap

import com.intellij.platform.dap.DapEvaluationContext
import com.intellij.platform.dap.DapEvaluationResult
import com.intellij.platform.dap.DapScope
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapStackFrame
import com.intellij.platform.dap.DapThread
import com.intellij.platform.dap.DapVariable
import com.intellij.platform.dap.FrameId
import com.intellij.platform.dap.StackFrameType
import com.intellij.platform.dap.TextPosition
import com.intellij.platform.dap.ValueKind
import com.jetbrains.dap.protocol.DapRequestFailedException
import com.jetbrains.dap.protocol.VariableAttribute
import gdscript.GdScriptBundle
import gdscript.dap.remote.GdFrameSelf
import gdscript.dap.remote.GdFrameSourceSelf
import gdscript.dap.remote.GdSelfCheckedFrame
import gdscript.dap.remote.readSelfInMembers
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.IOException

class GdFrameSelfTest {
    @Test
    fun testFrameEvaluationRequiresSelf(): Unit = withControlledTime {
        val context = unusedSessionContext()
        var members = emptyList<DapVariable>()
        val answer = DapEvaluationResult.Error("adapter error")
        val frame = TestFrame(answer) { members }
        val guarded = checked(frame)
        for (dump in listOf(emptyList(), listOf(variable("health")))) {
            members = dump
            assertEquals(GdFrameSelf.Absent, with(context) { guarded.run { checkSelf() } })
            val blocked = with(context) { guarded.run { evaluate("player.health") } }
            assertEquals(DapEvaluationResult.Error(GdScriptBundle.message("gdscript.debugger.error.evaluation.without.self")), blocked)
            assertTrue(frame.evaluations.isEmpty())
        }
        members = listOf(variable("self"))
        val expression = "  self.get(\"health\")  "
        assertSame(answer, with(context) { guarded.run { evaluate(expression) } })
        assertTrue(expression in frame.evaluations)
    }

    @Test
    fun testFailedMembersRequestIsRetriedUntilGodotSendsTheDump(): Unit = withControlledTime {
        val context = unusedSessionContext()
        val self = variable("self")
        val frame = TestFrame { if (it <= 2) throw DapRequestFailedException("unknown error") else listOf(self) }
        assertEquals(GdFrameSelf.Present(self), with(context) { checked(frame).run { checkSelf() } })
        assertEquals(DapEvaluationResult.Error("evaluated"), with(context) { checked(frame).run { evaluate("health") } })
        assertTrue("health" in frame.evaluations)
    }

    @Test
    fun testMembersRequestThatAlwaysFailsGivesUnavailable(): Unit = withControlledTime {
        var allowed = true
        val frame = TestFrame { throw DapRequestFailedException("unknown error") }
        val result = with(unusedSessionContext()) {
            readSelfInMembers(frame, retryAllowed = { allowed }, retryDelay = { allowed = false })
        }
        assertEquals(GdFrameSelf.Unavailable, result)
        assertTrue(frame.evaluations.isEmpty())
    }

    @Test
    fun testUnavailableMembersBlockTheEvaluationWithItsOwnMessage(): Unit = withControlledTime {
        val frame = TestFrame { throw IOException("the connection closed") }
        val blocked = with(unusedSessionContext()) { checked(frame).run { evaluate("health") } }
        assertEquals(DapEvaluationResult.Error(GdScriptBundle.message("gdscript.debugger.error.evaluation.self.unavailable")), blocked)
        assertTrue(frame.evaluations.isEmpty())
    }

    @Test
    fun testSourceRejectionSendsNoRequests(): Unit = withControlledTime {
        for (source in listOf(GdFrameSourceSelf.Absent, GdFrameSourceSelf.Unavailable)) {
            val frame = TestFrame { error("Unexpected Members request") }
            frame.scopesBody = { error("Unexpected scopes request") }
            val guarded = GdSelfCheckedFrame(frame, { source })
            val result = with(unusedSessionContext()) { guarded.run { evaluate("health", DapEvaluationContext.Watch) } }
            val key = when (source) {
                GdFrameSourceSelf.Absent -> "gdscript.debugger.error.evaluation.without.self"
                GdFrameSourceSelf.Unavailable -> "gdscript.debugger.error.evaluation.self.unavailable"
                GdFrameSourceSelf.Present -> error("Unexpected source decision")
            }
            assertEquals(DapEvaluationResult.Error(GdScriptBundle.message(key)), result)
            assertTrue(frame.evaluations.isEmpty())
        }
    }

    @Test
    fun testSourceDecisionFinishesBeforeTheDumpStarts(): Unit = withControlledTime {
        val entered = CompletableDeferred<Unit>()
        val source = CompletableDeferred<GdFrameSourceSelf>()
        var dumpAllowed = false
        val frame = TestFrame { listOf(variable("self")) }
        frame.scopesBody = { check(dumpAllowed) { "The dump preceded the source decision" }; null }
        val guarded = GdSelfCheckedFrame(frame, {
            entered.complete(Unit)
            source.await()
        })
        val work = async { with(unusedSessionContext()) { guarded.run { evaluate("health") } } }
        entered.await()
        dumpAllowed = true
        source.complete(GdFrameSourceSelf.Present)
        assertEquals(DapEvaluationResult.Error("evaluated"), work.await())
        assertTrue("health" in frame.evaluations)
    }

    @Test
    fun testTimeoutIncludesSourceScopesAndMembersReads(): Unit = withControlledTime {
        for (phase in listOf("source", "scopes", "members")) {
            val frame = TestFrame {
                if (phase == "members") awaitCancellation()
                error("Unexpected Members request")
            }
            frame.scopesBody = {
                if (phase == "scopes") awaitCancellation()
                null
            }
            val guarded = GdSelfCheckedFrame(frame, {
                if (phase == "source") awaitCancellation()
                GdFrameSourceSelf.Present
            })
            val result = with(unusedSessionContext()) { guarded.run { evaluate("health") } }
            assertEquals(DapEvaluationResult.Error(GdScriptBundle.message("gdscript.debugger.error.evaluation.self.timeout")), result)
            assertTrue(frame.evaluations.isEmpty())
        }
    }

    @Test
    fun testContextEvaluationKeepsTheExpressionAndContext(): Unit = withControlledTime {
        val frame = TestFrame { listOf(variable("self")) }
        val result = with(unusedSessionContext()) { checked(frame).run { evaluate(" health ", DapEvaluationContext.Hover) } }
        assertEquals(DapEvaluationResult.Error("evaluated"), result)
        assertTrue(" health " in frame.evaluations)
        assertTrue(DapEvaluationContext.Hover in frame.contexts)
    }

    @Test
    fun testCancelledSourceReadDoesNotPoisonAnotherConsumer(): Unit = withControlledTime {
        for (duringRetry in listOf(false, true)) {
            val entered = CompletableDeferred<Unit>()
            var first = true
            val frame = TestFrame {
                if (duringRetry && first) {
                    first = false
                    entered.complete(Unit)
                    throw DapRequestFailedException("not ready")
                }
                listOf(variable("self"))
            }
            val guarded = GdSelfCheckedFrame(frame, {
                if (!duringRetry && first) {
                    first = false
                    entered.complete(Unit)
                    awaitCancellation()
                }
                GdFrameSourceSelf.Present
            })
            var result: DapEvaluationResult? = null
            val work = launch { result = with(unusedSessionContext()) { guarded.run { evaluate("cancelled") } } }
            entered.await()
            work.cancelAndJoin()
            assertNull(result)
            assertEquals(DapEvaluationResult.Error("evaluated"), with(unusedSessionContext()) { guarded.run { evaluate("health") } })
            assertTrue("health" in frame.evaluations)
        }
    }

    private fun checked(frame: DapStackFrame) = GdSelfCheckedFrame(frame, { GdFrameSourceSelf.Present })

    /** A frame with one Members scope. [members] gets the 1-based number of the request. */
    private class TestFrame(
        private val answer: DapEvaluationResult = DapEvaluationResult.Error("evaluated"),
        private val members: suspend (Int) -> List<DapVariable>,
    ) : DapStackFrame {
        private var membersRequests = 0
        var scopesBody: (suspend () -> List<DapScope>?)? = null
        val evaluations = mutableListOf<String>()
        val contexts = mutableListOf<DapEvaluationContext>()

        private val scope = object : DapScope {
            override val frame: DapStackFrame get() = this@TestFrame
            override val isExpensive = false
            override val name = "Members"
            override suspend fun DapSessionContext.variables(): List<DapVariable> = members(++membersRequests)
        }

        override val id = FrameId(1)
        override val name = "frame"
        override val type = StackFrameType.Normal
        override val thread: DapThread get() = error("Unexpected thread call")
        override val source = null
        override val startPosition = TextPosition(1, 1)
        override val endPosition = TextPosition(1, 1)
        override suspend fun DapSessionContext.scopes(): List<DapScope> = scopesBody?.invoke() ?: listOf(scope)
        override suspend fun DapSessionContext.evaluate(expression: String, context: DapEvaluationContext): DapEvaluationResult {
            contexts.add(context)
            return evaluate(expression)
        }
        override suspend fun DapSessionContext.evaluate(expression: String): DapEvaluationResult {
            evaluations.add(expression)
            return answer
        }
    }

    private fun variable(nameArg: String): DapVariable = object : DapVariable {
        override val name = nameArg
        override val type = "Object"
        override val value = nameArg
        override val evaluateName: String? = null
        override val kind: ValueKind? = null
        override val attributes: List<VariableAttribute>? = null
    }
}
