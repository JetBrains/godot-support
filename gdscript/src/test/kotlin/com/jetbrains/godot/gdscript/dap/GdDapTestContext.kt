package com.jetbrains.godot.gdscript.dap

import com.intellij.platform.dap.DapEvaluationResult
import com.intellij.platform.dap.DapScope
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapStackFrame
import com.intellij.platform.dap.DapStructuredVariable
import com.intellij.platform.dap.DapThread
import com.intellij.platform.dap.DapVariable
import com.intellij.platform.dap.FrameId
import com.intellij.platform.dap.StackFrameType
import com.intellij.platform.dap.TextPosition
import com.intellij.platform.dap.ValueKind
import com.intellij.platform.dap.VariableBatch
import com.intellij.platform.dap.VariableFilter
import com.intellij.testFramework.common.timeoutRunBlocking
import com.jetbrains.dap.protocol.Capabilities
import com.jetbrains.dap.protocol.DapRemoteEndpoint
import com.jetbrains.dap.protocol.DapServer
import com.jetbrains.dap.protocol.VariableAttribute
import gdscript.dap.remote.GdFrameSourceSelf
import gdscript.dap.remote.GdSelfCheckedFrame
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import java.lang.reflect.Proxy
import java.util.PriorityQueue
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

/** Creates a frame whose source and Members dump permit the supplied evaluation. */
internal fun evaluationFrame(evaluation: suspend DapSessionContext.(String) -> DapEvaluationResult): GdSelfCheckedFrame {
    val self = object : DapVariable {
        override val name = "self"
        override val type = "Object"
        override val value = "self"
        override val evaluateName: String? = null
        override val kind: ValueKind? = null
        override val attributes: List<VariableAttribute>? = null
    }
    class EvaluationFrame : DapStackFrame {
        override val id = FrameId(1)
        override val name = "frame"
        override val type = StackFrameType.Normal
        override val source = null
        override val startPosition = TextPosition(1, 1)
        override val endPosition = startPosition
        override val thread: DapThread get() = error("Unexpected thread request")
        override suspend fun DapSessionContext.evaluate(expression: String): DapEvaluationResult = evaluation(expression)
        override suspend fun DapSessionContext.scopes(): List<DapScope> {
            val currentFrame = this@EvaluationFrame
            return listOf(object : DapScope {
                override val frame: DapStackFrame = currentFrame
                override val name = "Members"
                override val isExpensive = false
                override suspend fun DapSessionContext.variables(): List<DapVariable> = listOf(self)
            })
        }
    }
    return GdSelfCheckedFrame(EvaluationFrame(), { GdFrameSourceSelf.Present })
}

internal fun numberedSuccessAnswer(
    expression: String, answer: DapStructuredVariable, splitAt: Int = 3
): DapEvaluationResult {
    fun field(name: String, value: String): DapVariable = object : DapVariable by answer {
        override val name = name
        override val type = "int"
        override val value = value
    }
    val number = expression.substringAfterLast(", ").removeSuffix("]")
    val fields = listOf(field("size", "2"), answer, field("1", number))
    return DapEvaluationResult.Success(object : DapStructuredVariable, DapVariable by answer {
        override val name = "outer"
        override val type = "Array"
        override val value = "value"
        override val namedVariables = 0
        override val indexedVariables = fields.size
        override fun DapSessionContext.loadVariables(filter: VariableFilter?, batchSize: Int): Flow<VariableBatch> =
            if (splitAt == fields.size) flowOf(VariableBatch(fields, 0))
            else flowOf(VariableBatch(fields.take(splitAt), fields.size - splitAt), VariableBatch(fields.drop(splitAt), 0))
    })
}

/**
 * A [DapSessionContext] for code under test that needs the receiver but never reaches the adapter:
 * every server and endpoint call fails the test.
 *
 * The constructor is internal to the platform module, so the test builds the context reflectively
 * instead of widening the platform API.
 */
internal fun CoroutineScope.unusedSessionContext(): DapSessionContext {
    val constructor = DapSessionContext::class.java.declaredConstructors.single()
    constructor.isAccessible = true
    return constructor.newInstance(this, unused<DapServer>(), unused<DapRemoteEndpoint>(), MutableStateFlow(Capabilities())) as DapSessionContext
}

internal fun withControlledTime(body: suspend CoroutineScope.() -> Unit): Unit = timeoutRunBlocking {
    val dispatcher = ControlledTimeDispatcher()
    val work = async(dispatcher, block = body)
    while (!work.isCompleted) dispatcher.runNext()
    work.await()
}

@OptIn(InternalCoroutinesApi::class)
private class ControlledTimeDispatcher : CoroutineDispatcher(), Delay {
    private class Task(val time: Long, val order: Long, val action: Runnable) {
        var cancelled = false
    }

    private val tasks = PriorityQueue<Task>(compareBy({ it.time }, { it.order }))
    private var time = 0L
    private var order = 0L

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        tasks.add(Task(time, order++, block))
    }

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val handle = invokeOnTimeout(timeMillis, Runnable { continuation.resume(Unit) }, continuation.context)
        continuation.invokeOnCancellation { handle.dispose() }
    }

    override fun invokeOnTimeout(timeMillis: Long, block: Runnable, context: CoroutineContext): DisposableHandle {
        val task = Task(time + timeMillis, order++, block)
        tasks.add(task)
        return DisposableHandle { task.cancelled = true }
    }

    fun runNext() {
        val task = checkNotNull(tasks.poll()) { "The controlled coroutine has no scheduled work" }
        time = task.time
        if (!task.cancelled) task.action.run()
    }
}

private inline fun <reified T : Any> unused(): T {
    val type = T::class.java
    return Proxy.newProxyInstance(type.classLoader, arrayOf(type)) { proxy, method, args ->
        when (method.name) {
            "toString" -> "Unused ${type.simpleName}"
            "hashCode" -> System.identityHashCode(proxy)
            "equals" -> proxy === args?.firstOrNull()
            else -> error("Unexpected ${type.simpleName}.${method.name} call")
        }
    } as T
}
