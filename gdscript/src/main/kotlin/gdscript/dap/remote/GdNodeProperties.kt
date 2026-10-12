package gdscript.dap.remote

import com.intellij.platform.dap.DapInlineValueContext
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.DapStructuredVariable
import com.intellij.platform.dap.DapVariable
import com.intellij.platform.dap.xdebugger.DapXDebuggerPresentationFactory
import com.intellij.xdebugger.frame.XValueChildrenList
import gdscript.GdScriptBundle

internal sealed class GdPropertiesLoad {
    data class Success(val properties: XValueChildrenList) : GdPropertiesLoad()
    data class Failure(val message: String) : GdPropertiesLoad()
}

/**
 * Builds the node's own DAP property rows.
 * Uses [knownVariable] for self. Other node rows resolve a DAP variable by ObjectID when the user opens their properties.
 */
internal suspend fun DapSessionContext.loadNodeProperties(
    executor: DapSessionExecutor,
    evaluator: GdSelfCheckedFrame,
    factory: DapXDebuggerPresentationFactory,
    objectId: Long,
    knownVariable: DapVariable?,
    self: Boolean = false,
    selfPropertyInlineContext: DapInlineValueContext? = null,
): GdPropertiesLoad {
    // 1. Select or resolve the node variable.
    val nodeVar = if (knownVariable != null) {
        // Use the variable supplied by the self row.
        knownVariable as? DapStructuredVariable
            ?: return GdPropertiesLoad.Failure(GdScriptBundle.message("gdscript.debugger.scene.tree.error.properties.wrong.type"))
    } else {
        // Resolve other rows by their ObjectID when the user opens the properties.
        when (val instOutcome = evaluateStructuredOutcome(evaluator, GdDapExpressions.instance(objectId))) {
            is GdEvalOutcome.Failed -> {
                return GdPropertiesLoad.Failure(
                    GdScriptBundle.message("gdscript.debugger.scene.tree.error.properties.failed", instOutcome.message)
                )
            }

            is GdEvalOutcome.Missing -> {
                return GdPropertiesLoad.Failure(
                    GdScriptBundle.message("gdscript.debugger.scene.tree.error.properties.missing")
                )
            }

            is GdEvalOutcome.Structured -> instOutcome.variable
        }
    }

    // 2. Read its variables and create property rows.
    val variables = when (val load = loadVariablesBounded(nodeVar)) {
        is GdVariablesLoad.Success -> load.variables
        is GdVariablesLoad.Failed -> return GdPropertiesLoad.Failure(
            GdScriptBundle.message("gdscript.debugger.scene.tree.error.properties.failed", load.message)
        )
    }

    val properties = XValueChildrenList()
    val watchPaths = GdWatchPath.nodeProperties(if (self) "self" else GdDapExpressions.instance(objectId)).properties("", variables)
    for ((index, variable) in variables.withIndex()) {
        properties.add(
            GdDapXValue(
                factory, executor, variable,
                owner = GdOwnerExpression(GdDapExpressions.instance(objectId)), frame = evaluator,
                watchContext = GdWatchContext(watchPaths[index], evaluator, executor),
                contextForOwnProperties = categoryPropertyContext(variable, selfPropertyInlineContext)
            )
        )
    }

    return GdPropertiesLoad.Success(properties)
}
