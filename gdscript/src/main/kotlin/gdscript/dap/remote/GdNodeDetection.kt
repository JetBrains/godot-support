package gdscript.dap.remote

import com.intellij.diagnostic.rethrowControlFlowException
import com.intellij.openapi.diagnostic.logger
import com.intellij.platform.dap.DapSessionContext
import gdscript.GdScriptBundle

private val LOG = logger<GdNodeDetection>()

private const val UNKNOWN_CLASS_NAME: String = "<unknown>"

/** Separates a failed inspection from a confirmed non-Node. */
internal sealed interface GdNodeDetection {
    data class Node(val node: GdNodeDescriptor) : GdNodeDetection
    data object NonNode : GdNodeDetection
    data class Failed(val message: String) : GdNodeDetection
}

/** Inspects the frame's base instance. Call this only after [hasSelfInMembers] returns true. */
internal suspend fun DapSessionContext.detectFrameNode(evaluator: GdSelfCheckedFrame): GdNodeDetection {
    try {
        val variables = when (val outcome = evaluateStructuredOutcome(evaluator, GdDapExpressions.NODE_DETECTION)) {
            is GdEvalOutcome.Structured -> when (val load = loadVariablesBounded(outcome.variable)) {
                is GdVariablesLoad.Success -> load.variables
                is GdVariablesLoad.Failed -> return GdNodeDetection.Failed(load.message)
            }
            is GdEvalOutcome.Missing -> return GdNodeDetection.Failed(GdScriptBundle.message("gdscript.debugger.error.unknown"))
            is GdEvalOutcome.Failed -> return GdNodeDetection.Failed(outcome.message)
        }
        val reader = GdArrayReader(variables)
        val isNode = reader.getOrNull(0)?.value
        if (isNode == "false") return GdNodeDetection.NonNode

        val objectId = reader.getOrNull(1)?.value?.toLongOrNull()
        if (isNode != "true" || objectId == null) {
            return GdNodeDetection.Failed(GdScriptBundle.message("gdscript.debugger.error.unknown"))
        }

        val className = reader.getOrNull(2)?.value ?: UNKNOWN_CLASS_NAME
        val nodeName = reader.getOrNull(3)?.value?.takeIf { it.isNotBlank() }
        LOG.trace("Frame base instance is a Node: $className #$objectId (name=${nodeName ?: "<unknown>"})")
        return GdNodeDetection.Node(GdNodeDescriptor(objectId = objectId, nodeName = nodeName, className = className))
    }
    catch (e: Throwable) {
        rethrowControlFlowException(e)
        return GdNodeDetection.Failed(e.message ?: GdScriptBundle.message("gdscript.debugger.error.unknown"))
    }
}
