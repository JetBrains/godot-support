package gdscript.dap.remote

import com.intellij.diagnostic.rethrowControlFlowException
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.util.registry.Registry
import com.intellij.platform.dap.DapScope
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapStructuredVariable
import com.intellij.platform.dap.DapVariable
import com.jetbrains.dap.protocol.DapException
import com.jetbrains.dap.protocol.DapRequestFailedException
import gdscript.GdScriptBundle
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.TimeSource

private val LOG = logger<GdVariablesLoad>()

/** The result of one complete variables read. */
internal sealed interface GdVariablesLoad {
    data class Success(val variables: List<DapVariable>) : GdVariablesLoad
    data class Failed(val message: String) : GdVariablesLoad
}

/** Limits the complete collection with the platform evaluate timeout setting. */
internal suspend fun DapSessionContext.loadVariablesBounded(variable: DapStructuredVariable): GdVariablesLoad =
    readVariablesBounded { variable.run { loadVariables(null, 0).toList() }.flatMap { it.variables } }

/** Retries rejected scope reads. Each attempt keeps the platform evaluate timeout. */
internal suspend fun DapSessionContext.loadVariablesBounded(
    scope: DapScope,
    retryAllowed: () -> Boolean = { true },
    retryDelay: suspend () -> Unit = { delay(GdSelfCheckPolicy.MEMBERS_RETRY_DELAY) },
    timeSource: TimeSource = TimeSource.Monotonic,
): GdVariablesLoad {
    val start = timeSource.markNow()
    return try {
        retryRejectedVariables({ retryAllowed() && start.elapsedNow() < GdSelfCheckPolicy.MEMBERS_RETRY_LIMIT }, retryDelay) {
            readVariablesBounded { scope.run { variables() } }
        }
    }
    catch (e: Throwable) {
        rethrowControlFlowException(e)
        if (e is DapException) LOG.debug("The scope variables request failed", e)
        else LOG.warn("Unexpected error computing scope variables", e)
        GdVariablesLoad.Failed(e.message ?: GdScriptBundle.message("gdscript.debugger.error.unknown"))
    }
}

/**
 * Retries only rejected responses. The caller limits new attempts and owns cancellation.
 * Godot starts a variable dump in `DebugAdapterParser::req_scopes`.
 * While the dump runs, `req_variables` returns an empty dictionary. An unknown variable reference produces an UNKNOWN error.
 * See `debug_adapter_parser.cpp`, `req_scopes` and `req_variables`.
 * A rejected response can therefore mean that the dump is not ready. A successful empty scope remains a valid result.
 */
internal suspend fun <T> retryRejectedVariables(
    retryAllowed: () -> Boolean,
    retryDelay: suspend () -> Unit,
    read: suspend () -> T,
): T {
    while (true) {
        currentCoroutineContext().ensureActive()
        try {
            return read()
        }
        catch (e: DapRequestFailedException) {
            if (!retryAllowed()) throw e
            retryDelay()
            if (!retryAllowed()) throw e
        }
    }
}

private suspend fun readVariablesBounded(read: suspend () -> List<DapVariable>): GdVariablesLoad =
    withTimeoutOrNull(Registry.intValue("dap.timeout.evaluate", 30_000).toLong()) {
        GdVariablesLoad.Success(read())
    } ?: GdVariablesLoad.Failed(GdScriptBundle.message("gdscript.debugger.error.command.timed.out"))
