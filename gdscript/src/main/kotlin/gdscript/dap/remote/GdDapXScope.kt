package gdscript.dap.remote

import com.intellij.diagnostic.rethrowControlFlowException
import com.intellij.openapi.diagnostic.logger
import com.intellij.platform.dap.DapInlineValueLocator
import com.intellij.platform.dap.DapScope
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.xdebugger.DapXDebuggerPresentationFactory
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XValueChildrenList
import com.intellij.xdebugger.frame.XValueGroup
import gdscript.GdScriptBundle

private val LOG = logger<GdDapXScope>()

/** A scope group in a paused frame. A supplied recovery frame enables local owner expressions and object probes. */
internal class GdDapXScope(
    private val factory: DapXDebuggerPresentationFactory,
    private val executor: DapSessionExecutor,
    private val scope: DapScope,
    private val index: Int,
    private val frame: GdSelfCheckedFrame? = null,
    private val inlineValueLocator: DapInlineValueLocator? = null,
    private val scopeLoader: suspend DapSessionContext.(DapScope, retryAllowed: () -> Boolean) -> GdVariablesLoad = { scope, retryAllowed ->
        loadVariablesBounded(scope, retryAllowed = retryAllowed)
    },
    private val watchFrame: GdSelfCheckedFrame? = frame,
) : XValueGroup(scope.name) {
    private suspend fun DapSessionContext.localNames(): Set<String>? {
        return try {
            val locals = scope.frame.run { scopes() }.firstOrNull { it.name == "Locals" } ?: return emptySet()
            when (val load = loadVariablesBounded(locals)) {
                is GdVariablesLoad.Success -> load.variables.mapTo(HashSet()) { it.name }
                is GdVariablesLoad.Failed -> null
            }
        }
        catch (e: Exception) {
            rethrowControlFlowException(e)
            LOG.debug("Could not check local names for Globals watches", e)
            null
        }
    }

    override fun isAutoExpand(): Boolean = index == 0 && !scope.isExpensive

    override fun computeChildren(node: XCompositeNode) {
        executor.computeChildrenWithCompletion(
            node = node,
            log = LOG,
            subject = "scope variables",
            reportCancellation = true,
            errorMessage = { GdScriptBundle.message("gdscript.debugger.error.unknown") },
        ) { target ->
            if (target.isObsolete) return@computeChildrenWithCompletion
            val load = scopeLoader(scope) { !target.isObsolete }
            if (target.isObsolete) return@computeChildrenWithCompletion
            val children = XValueChildrenList()
            val probes = mutableListOf<Pair<GdDapXValue, String>>()
            val variables = when (load) {
                is GdVariablesLoad.Success -> load.variables
                is GdVariablesLoad.Failed -> {
                    target.setErrorMessage(GdScriptBundle.message("gdscript.debugger.error.evaluation.failed", load.message))
                    return@computeChildrenWithCompletion
                }
            }
            val inlineValueContext = createInlineValueContext(scope.frame, inlineValueLocator)
            val hiddenGlobals = if (scope.name == "Globals") localNames() else emptySet()
            val watchPaths = GdWatchPath.scope(scope.name, variables, hiddenGlobals)
            variables.forEachIndexed { index, variable ->
                val expression = if (frame != null) GdLocalExpressions.name(variable.name) else null
                val owner = expression?.let(::GdOwnerExpression)
                val row = GdDapXValue(factory, executor, variable, owner = owner, frame = frame,
                                     watchContext = GdWatchContext(watchPaths[index], watchFrame ?: scope.frame, executor))
                // Each pair maps a batched answer to its row by position.
                if (GdObjectProbe.isProbeTarget(variable, owner)) probes.add(row to expression!!)
                if (inlineValueContext != null) row.inlineValueContext = inlineValueContext
                children.add(row)
            }
            target.addChildren(children, true)
            if (frame != null && probes.isNotEmpty()) {
                val presentations = with(GdObjectProbe) { load(frame, probes.map { it.second }) }
                presentations?.forEachIndexed { index, presentation -> probes[index].first.updatePresentation(presentation) }
            }
        }
    }
}
