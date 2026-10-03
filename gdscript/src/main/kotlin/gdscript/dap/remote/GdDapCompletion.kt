package gdscript.dap.remote

import com.intellij.diagnostic.rethrowControlFlowException
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.diagnostic.isControlFlowException
import com.intellij.openapi.diagnostic.logger
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.DapSessionStoppedException
import com.intellij.ui.SimpleTextAttributes
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator.XEvaluationCallback
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XDebuggerTreeNodeHyperlink
import com.intellij.xdebugger.frame.XValue
import com.intellij.xdebugger.frame.XValueChildrenList
import com.intellij.xdebugger.frame.XValueNode
import com.intellij.xdebugger.frame.presentation.XValuePresentation
import com.jetbrains.dap.protocol.DapConnectionClosedException
import com.jetbrains.dap.protocol.RequestCancelledByPeerException
import gdscript.GdScriptBundle
import kotlinx.coroutines.CancellationException
import org.jetbrains.concurrency.AsyncPromise
import org.jetbrains.concurrency.Promise
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.Icon

private val LOG = logger<GdCompletingChildrenNode>()

/** Calls [complete] when the work ends, including work that cannot start. */
private fun DapSessionExecutor.postWithCompletion(
    complete: (Throwable?) -> Unit,
    body: suspend DapSessionContext.() -> Unit,
) {
    fun finish(failure: Throwable?) {
        try {
            complete(failure)
        }
        catch (e: Throwable) {
            // A completion handler must not throw, including when the consumer throws.
            LOG.warn("Could not deliver the debugger answer", e)
        }
    }

    val work = try {
        postAsync(body)
    }
    catch (e: DapSessionStoppedException) {
        finish(e)
        return
    }
    work.invokeOnCompletion { finish(it) }
}

/**
 * Delivers one presentation, also when the work cannot start.
 * Cancellation shows the cancelled message. A stopped session or closed connection shows the stopped message.
 * Other failures log WARN and show the evaluation error. A delivered presentation stays unchanged.
 */
internal fun DapSessionExecutor.computePresentationWithCompletion(
    node: XValueNode,
    log: Logger,
    subject: String,
    body: suspend DapSessionContext.(target: XValueNode) -> Unit,
) {
    val delivered = AtomicBoolean(false)
    val target = object : XValueNode by node {
        override fun setPresentation(icon: Icon?, type: String?, value: String, hasChildren: Boolean) {
            if (delivered.compareAndSet(false, true)) node.setPresentation(icon, type, value, hasChildren)
        }

        override fun setPresentation(icon: Icon?, presentation: XValuePresentation, hasChildren: Boolean) {
            if (delivered.compareAndSet(false, true)) node.setPresentation(icon, presentation, hasChildren)
        }

        override fun isObsolete(): Boolean {
            return node.isObsolete
        }
    }
    fun complete(failure: Throwable?) {
        if (delivered.get()) return
        val message = when (failure) {
            is DapSessionStoppedException, is DapConnectionClosedException ->
                GdScriptBundle.message("gdscript.debugger.error.evaluation.session.stopped")
            is CancellationException -> GdScriptBundle.message("gdscript.debugger.error.cancelled")
            else -> GdScriptBundle.message("gdscript.debugger.scene.tree.error.evaluation.failed",
                failure?.message ?: GdScriptBundle.message("gdscript.debugger.error.unknown"))
        }
        target.setPresentation(null, null, message, false)
    }
    postWithCompletion(complete = ::complete) {
        try {
            body(target)
        }
        catch (e: Throwable) {
            rethrowControlFlowException(e)
            if (e !is DapSessionStoppedException && e !is DapConnectionClosedException) {
                log.warn("Could not resolve $subject presentation", e)
            }
            complete(e)
        }
    }
}

/**
 * Delivers one final batch when the body ends, unless the body already sends its last content batch.
 * The wrapper ignores subsequent answers. The body can set an error without sending an empty final batch.
 *
 * Peer cancellation shows the cancelled message. Caller cancellation and session stop stay quiet unless [reportCancellation] is true.
 * A closed connection stays quiet. Other failures log WARN and use [errorMessage].
 * Partial batches remain available to the consumer before the error and the final batch.
 */
internal fun DapSessionExecutor.computeChildrenWithCompletion(
    node: XCompositeNode,
    log: Logger,
    subject: String,
    reportCancellation: Boolean = false,
    errorMessage: (Throwable) -> String,
    body: suspend DapSessionContext.(target: XCompositeNode) -> Unit,
) {
    val target = GdCompletingChildrenNode(node)
    postWithCompletion(complete = { failure ->
        target.complete {
            try {
                if (failure is RequestCancelledByPeerException || reportCancellation && failure is CancellationException) {
                    if (!node.isObsolete) node.setErrorMessage(GdScriptBundle.message("gdscript.debugger.error.cancelled"))
                }
                else if (reportCancellation && failure is DapSessionStoppedException) {
                    if (!node.isObsolete) node.setErrorMessage(GdScriptBundle.message("gdscript.debugger.error.evaluation.session.stopped"))
                }
                else if (failure is DapConnectionClosedException) {
                    log.debug("The DAP connection closed computing $subject", failure)
                }
                else if (failure != null && !failure.isControlFlowException && failure !is DapSessionStoppedException) {
                    log.warn("Unexpected error computing $subject", failure)
                    node.setErrorMessage(errorMessage(failure))
                }
            }
            finally {
                node.addChildren(XValueChildrenList.EMPTY, true)
            }
        }
    }) { body(target) }
}

/**
 * Delivers one evaluation answer, also when the work cannot start.
 * Cancellation and session stop use their specific messages. Other failures use the evaluation error.
 * A delivered result stays unchanged, also if a later optional probe fails.
 */
internal fun DapSessionExecutor.evaluateWithCompletion(
    callback: XEvaluationCallback,
    body: suspend DapSessionContext.(answer: XEvaluationCallback) -> Unit,
) {
    val delivered = AtomicBoolean(false)
    val answer = object : XEvaluationCallback {
        override fun evaluated(result: XValue) {
            if (delivered.compareAndSet(false, true)) callback.evaluated(result)
        }

        override fun errorOccurred(errorMessage: String) {
            if (delivered.compareAndSet(false, true)) callback.errorOccurred(errorMessage)
        }
    }
    postWithCompletion(complete = { failure ->
        if (!delivered.get()) {
            val message = when (failure) {
                is CancellationException -> GdScriptBundle.message("gdscript.debugger.error.cancelled")
                is DapSessionStoppedException -> GdScriptBundle.message("gdscript.debugger.error.evaluation.session.stopped")
                else -> GdScriptBundle.message("gdscript.debugger.error.evaluation.failed",
                    failure?.message ?: GdScriptBundle.message("gdscript.debugger.error.unknown"))
            }
            answer.errorOccurred(message)
        }
    }) { body(answer) }
}

/**
 * Settles the promise when the work ends. Cancellation cancels it. Session stop uses the stopped message.
 * Other failures reject it with the cause. A normal return without a result rejects it with the unknown error.
 */
internal fun <T> DapSessionExecutor.computePromiseWithCompletion(
    body: suspend DapSessionContext.(answer: AsyncPromise<T>) -> Unit,
): Promise<T> {
    val promise = AsyncPromise<T>()
    postWithCompletion(complete = { failure ->
        when (failure) {
            is CancellationException -> promise.cancel()
            is DapSessionStoppedException -> promise.setError(GdScriptBundle.message("gdscript.debugger.error.evaluation.session.stopped"))
            null -> if (!promise.isDone) promise.setError(GdScriptBundle.message("gdscript.debugger.error.unknown"))
            else -> promise.setError(failure)
        }
    }) { body(promise) }
    return promise
}

/** Forwards node operations while the computation is open. Each computation has its own terminal state. */
private class GdCompletingChildrenNode(private val delegate: XCompositeNode) : XCompositeNode by delegate {
    private val completed = AtomicBoolean(false)
    private val lock = Any()

    fun complete(deliver: () -> Unit) = synchronized(lock) {
        if (completed.compareAndSet(false, true)) deliver()
    }

    private fun ifOpen(deliver: () -> Unit) = synchronized(lock) {
        if (!completed.get()) deliver()
    }

    override fun addChildren(children: XValueChildrenList, last: Boolean) {
        if (last) complete { delegate.addChildren(children, true) }
        else ifOpen { delegate.addChildren(children, false) }
    }

    override fun setAlreadySorted(alreadySorted: Boolean) = ifOpen { delegate.setAlreadySorted(alreadySorted) }

    override fun setErrorMessage(errorMessage: String) = ifOpen { delegate.setErrorMessage(errorMessage) }

    override fun setErrorMessage(errorMessage: String, link: XDebuggerTreeNodeHyperlink?) =
        ifOpen { delegate.setErrorMessage(errorMessage, link) }

    override fun setMessage(message: String, icon: Icon?, attributes: SimpleTextAttributes, link: XDebuggerTreeNodeHyperlink?) =
        ifOpen { delegate.setMessage(message, icon, attributes, link) }

    override fun tooManyChildren(remaining: Int) = ifOpen { delegate.tooManyChildren(remaining) }

    override fun tooManyChildren(remaining: Int, addNextChildren: Runnable) =
        ifOpen { delegate.tooManyChildren(remaining, addNextChildren) }

    override fun isObsolete(): Boolean = delegate.isObsolete
}
