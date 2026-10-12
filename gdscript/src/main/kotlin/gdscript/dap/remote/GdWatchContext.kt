package gdscript.dap.remote

import com.intellij.diagnostic.rethrowControlFlowException
import com.intellij.openapi.util.registry.Registry
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.DapSessionStoppedException
import com.intellij.platform.dap.DapStackFrame
import com.intellij.xdebugger.XDebuggerUtil
import com.intellij.xdebugger.XExpression
import com.intellij.xdebugger.evaluation.EvaluationMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withTimeoutOrNull
import org.jetbrains.concurrency.AsyncPromise
import org.jetbrains.concurrency.Promise
import org.jetbrains.concurrency.resolvedPromise
import kotlin.time.Duration.Companion.milliseconds

/** Retains the frame and the pause executor independently of probe eligibility. */
internal data class GdWatchContext(val path: GdWatchPath, val frame: DapStackFrame, val executor: DapSessionExecutor) {
    /** Known paths need no checks. Pending reads require the row's current stop and run on its pause executor. */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun calculateEvaluationExpression(report: (String) -> Unit): Promise<XExpression> {
        if (!path.requiresKeyRead) return path.calculateEvaluationExpression(report)
        fun failure(reason: String) {
            report("No watch for ${path.role}: $reason")
        }
        val checkedFrame = frame as? GdSelfCheckedFrame
        if (checkedFrame == null) {
            failure("no checked frame for dictionary keys")
            return resolvedPromise()
        }
        val promise = AsyncPromise<XExpression>()
        val work = try {
            executor.postAsync {
                try {
                    withTimeoutOrNull(Registry.intValue("dap.timeout.evaluate", 30_000).milliseconds) {
                        val expression = path.resolveKeys { parent, position, labels ->
                            currentCoroutineContext().ensureActive()
                            if (checkedFrame.watchStop?.isCurrent() == false)
                                return@resolveKeys GdWatchReadOutcome.Failed("the row's stop is no longer current")
                            val key = readDictionaryKey(checkedFrame, parent, position, labels)
                            currentCoroutineContext().ensureActive()
                            if (checkedFrame.watchStop?.isCurrent() == false)
                                GdWatchReadOutcome.Failed("the row's stop is no longer current")
                            else key
                        }
                        currentCoroutineContext().ensureActive()
                        when (expression) {
                            is GdWatchReadOutcome.Success -> GdWatchReadOutcome.Success(
                                XDebuggerUtil.getInstance().createExpression(expression.value, null, null, EvaluationMode.EXPRESSION)
                            )
                            is GdWatchReadOutcome.Failed -> expression
                        }
                    } ?: GdWatchReadOutcome.Failed("dictionary key read timed out")
                }
                catch (e: Throwable) {
                    rethrowControlFlowException(e)
                    GdWatchReadOutcome.Failed("${e.javaClass.simpleName}: ${e.message ?: "dictionary key read failed"}")
                }
            }
        }
        catch (_: DapSessionStoppedException) {
            failure("the session stopped")
            promise.setResult(null)
            return promise
        }
        // The handler also completes work that the pause cancels before its body starts.
        work.invokeOnCompletion { cause ->
            if (cause == null) {
                when (val outcome = work.getCompleted()) {
                    is GdWatchReadOutcome.Success -> {
                        if (checkedFrame.watchStop?.isCurrent() == false) {
                            failure("the row's stop is no longer current")
                            promise.setResult(null)
                        }
                        else promise.setResult(outcome.value)
                    }
                    is GdWatchReadOutcome.Failed -> {
                        failure(outcome.reason)
                        promise.setResult(null)
                    }
                }
            }
            else {
                failure("${cause.javaClass.simpleName}: ${cause.message ?: "dictionary key read failed"}")
                promise.setResult(null)
            }
        }
        return promise
    }
}
