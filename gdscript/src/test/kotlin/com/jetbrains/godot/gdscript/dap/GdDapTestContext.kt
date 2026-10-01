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
import kotlinx.coroutines.supervisorScope
import java.lang.reflect.Proxy
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.DapSessionStoppedException
import com.intellij.platform.dap.DapStackFramesChunk
import com.intellij.platform.dap.DapThreadState
import com.intellij.platform.dap.SourcePosition
import com.intellij.platform.dap.StepInTargetId
import com.intellij.platform.dap.StepSize
import com.intellij.platform.dap.ThreadId
import com.intellij.ui.SimpleTextAttributes
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator.XEvaluationCallback
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XDebuggerTreeNodeHyperlink
import com.intellij.xdebugger.frame.XNamedValue
import com.intellij.xdebugger.frame.XNavigatable
import com.intellij.xdebugger.frame.XValue
import com.intellij.xdebugger.frame.XValueChildrenList
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import javax.swing.Icon
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

internal fun withExecutor(body: suspend (ControlledExecutor) -> Unit): Unit = timeoutRunBlocking {
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

internal class ControlledExecutor(private val scope: CoroutineScope) : DapSessionExecutor {
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

internal class RecordingCallback : XEvaluationCallback {
    val values = mutableListOf<XValue>()
    val errors = mutableListOf<String>()
    val answerCount get() = values.size + errors.size
    override fun evaluated(result: XValue) { values.add(result) }
    override fun errorOccurred(errorMessage: String) { errors.add(errorMessage) }
}

internal class RecordingNode(private val onBatch: (XValueChildrenList, Boolean) -> Unit = { _, _ -> }) : XCompositeNode {
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
    @Suppress("OVERRIDE_DEPRECATION")
    override fun tooManyChildren(remaining: Int) = Unit
    override fun isObsolete() = obsolete
}

internal class TestThread : DapThread {
    override val id = ThreadId(1)
    override val name = "thread"
    override var state: DapThreadState = DapThreadState.Paused(null)
    override val topFrame: DapStackFrame? = null
    @Suppress("RedundantNullableReturnType")
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

internal class TestFrame(
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
    var stoppedLine = 1
    override val startPosition get() = TextPosition(stoppedLine, 1)
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

internal fun positionOf(value: XNamedValue): Int? {
    var offset: Int? = null
    value.computeSourcePosition(XNavigatable { offset = it?.offset })
    return offset
}

internal fun testScope(
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
internal fun stoppedSession(): DapSessionStoppedException =
    DapSessionStoppedException::class.java.getDeclaredConstructor().apply { isAccessible = true }.newInstance()

internal fun numberedAnswer(expression: String, fields: List<DapVariable>): DapEvaluationResult {
    val inner = structuredVariable("0", "Array", listOf(variable("size", "int", fields.size.toString())) + fields)
    return numberedSuccessAnswer(expression, inner)
}

internal fun structuredVariable(
    name: String = "result", type: String = "Object", children: List<DapVariable>? = null
): DapStructuredVariable = object : DapStructuredVariable, DapVariable by variable(name, type) {
    override val namedVariables = children?.size ?: 1
    override val indexedVariables = 0
    override fun DapSessionContext.loadVariables(filter: VariableFilter?, batchSize: Int): Flow<VariableBatch> =
        flowOf(VariableBatch(children ?: error("Unexpected variable request"), 0))
}

internal fun variable(name: String, type: String, value: String = "value"): DapVariable = object : DapVariable {
    override val name = name
    override val type = type
    override val value = value
    override val evaluateName: String? = null
    override val kind: ValueKind? = null
    override val attributes: List<VariableAttribute>? = null
}
