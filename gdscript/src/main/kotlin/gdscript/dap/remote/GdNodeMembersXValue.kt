package gdscript.dap.remote

import com.intellij.openapi.diagnostic.logger
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.DapVariable
import com.intellij.platform.dap.xdebugger.DapXDebuggerPresentationFactory
import com.intellij.xdebugger.XExpression
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XNamedValue
import com.intellij.xdebugger.frame.XValueChildrenList
import com.intellij.xdebugger.frame.XValueNode
import com.intellij.xdebugger.frame.XValuePlace
import gdscript.GdScriptBundle
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import org.jetbrains.concurrency.Promise

private val LOG = logger<GdNodeMembersXValue>()

/** Outcome of resolving the node a [GdNodeMembersXValue] row stands for. */
internal sealed interface GdNodeResolution {
    data class Resolved(val descriptor: GdNodeDescriptor, val variable: DapVariable?) : GdNodeResolution

    /** Shows the localized [message] in a row that cannot expand. */
    data class Absent(val message: String) : GdNodeResolution
}

/**
 * Shows self, a child, or a parent with Children, Parent, and inline DAP properties, in that order.
 * [GdSceneTreeNodeXValue] instead shows children inline and properties in a bottom group.
 *
 * To add a node row kind, supply its resolution and presentation style through a factory in this class.
 * Use a known [GdNodeDescriptor] when available. Otherwise resolve it once through the session executor.
 * Pass a [GdSelfCheckedFrame] to every operation that evaluates.
 * Use the completion helpers for debugger callbacks and [addChildRows] for child lists.
 * [addChildRows] documents the child layout. [GdDapStackFrame.emitRows] documents the batch ordering rule.
 */
internal class GdNodeMembersXValue private constructor(
    name: String,
    private val executor: DapSessionExecutor,
    private val evaluator: GdSelfCheckedFrame,
    private val factory: DapXDebuggerPresentationFactory,
    resolutionProvider: () -> Deferred<GdNodeResolution>,
    private val presentationStyle: (GdNodeDescriptor) -> GdNodeRow,
) : XNamedValue(name) {

    /**
     * Shares one resolution across presentation, expansion, and the watch expression.
     * Start it on first use inside executor work. The provider can submit work and reject a stopped session.
     */
    private val resolutionDeferred: Deferred<GdNodeResolution> by lazy(resolutionProvider)

    companion object {
        /**
         * Uses a descriptor from the parent's indexed read. Resolves its DAP variable only on expansion.
         */
        fun child(
            descriptor: GdNodeDescriptor,
            executor: DapSessionExecutor,
            evaluator: GdSelfCheckedFrame,
            factory: DapXDebuggerPresentationFactory,
        ): GdNodeMembersXValue = GdNodeMembersXValue(
            name = descriptor.nodeName ?: descriptor.className,
            executor = executor,
            evaluator = evaluator,
            factory = factory,
            resolutionProvider = { CompletableDeferred(GdNodeResolution.Resolved(descriptor, variable = null)) },
            presentationStyle = GdNodePresentation::byClassName,
        )

        /**
         * Creates the Parent row for [objectId]. Starts its resolution on first use.
         */
        fun parentOf(
            objectId: Long,
            executor: DapSessionExecutor,
            evaluator: GdSelfCheckedFrame,
            factory: DapXDebuggerPresentationFactory,
        ): GdNodeMembersXValue = GdNodeMembersXValue(
            name = "Parent",
            executor = executor,
            evaluator = evaluator,
            factory = factory,
            resolutionProvider = { executor.postAsync { resolveParent(evaluator, objectId) } },
            presentationStyle = GdNodePresentation::byNodeName,
        )

        /**
         * Creates the self row from the frame's existing identity and variable.
         * Uses the node name as value text when available. Otherwise uses the class name.
         */
        fun selfOf(
            objectId: Long,
            nodeName: String?,
            className: String,
            selfVariable: DapVariable,
            executor: DapSessionExecutor,
            evaluator: GdSelfCheckedFrame,
            factory: DapXDebuggerPresentationFactory,
        ): GdNodeMembersXValue {
            val descriptor = GdNodeDescriptor(
                objectId = objectId,
                nodeName = nodeName,
                className = className
            )
            val presentationStyle: (GdNodeDescriptor) -> GdNodeRow = if (nodeName != null) {
                GdNodePresentation::byNodeName
            } else {
                GdNodePresentation::byClassName
            }
            return GdNodeMembersXValue(
                name = GdSelfCheckPolicy.SELF_VARIABLE_NAME,
                executor = executor,
                evaluator = evaluator,
                factory = factory,
                resolutionProvider = { CompletableDeferred(GdNodeResolution.Resolved(descriptor, selfVariable)) },
                presentationStyle = presentationStyle,
            )
        }
    }

    override fun computePresentation(node: XValueNode, place: XValuePlace) {
        executor.computePresentationWithCompletion(node, LOG, "node row") { target ->
            when (val resolution = resolutionDeferred.await()) {
                is GdNodeResolution.Resolved -> {
                    val row = presentationStyle(resolution.descriptor)
                    target.setPresentation(row.icon, row.presentation, true)
                }

                is GdNodeResolution.Absent -> target.setPresentation(null, null, resolution.message, false)
            }
        }
    }

    override fun computeChildren(node: XCompositeNode) {
        executor.computeChildrenWithCompletion(
            node = node,
            log = LOG,
            subject = "node children",
            errorMessage = ::childLoadErrorMessage,
        ) { target ->
            when (val resolution = resolutionDeferred.await()) {
                is GdNodeResolution.Resolved -> addResolvedChildren(target, resolution)

                is GdNodeResolution.Absent -> Unit
            }
        }
    }

    /** Emits Children, Parent, and properties in separate batches. See [GdDapStackFrame.emitRows]. */
    private suspend fun DapSessionContext.addResolvedChildren(target: XCompositeNode, resolution: GdNodeResolution.Resolved) {
        val descriptor = resolution.descriptor

        // 1. Add the Children group.
        target.setAlreadySorted(true)
        val childrenGroup = XValueChildrenList()
        childrenGroup.addTopGroup(GdChildrenXValueGroup(descriptor.objectId, executor, evaluator, factory))
        target.addChildren(childrenGroup, false)

        // 2. Add the Parent row.
        val parentRow = XValueChildrenList()
        parentRow.addTopValue(parentOf(descriptor.objectId, executor, evaluator, factory))
        target.addChildren(parentRow, false)

        // 3. Add the node's DAP properties inline.
        val properties = loadNodeProperties(executor, evaluator, factory, descriptor.objectId, resolution.variable)
        when (properties) {
            is GdPropertiesLoad.Success -> target.addChildren(properties.properties, true)

            is GdPropertiesLoad.Failure -> target.setErrorMessage(properties.message)
        }
    }

    override fun getEvaluationExpression(): String? = null

    override fun calculateEvaluationExpression(): Promise<XExpression> = executor.computeNodeEvaluationExpression { reject ->
        when (val resolution = resolutionDeferred.await()) {
            is GdNodeResolution.Resolved -> resolution.descriptor.objectId
            is GdNodeResolution.Absent -> {
                reject(resolution.message)
                null
            }
        }
    }
}

/**
 * Resolves the parent of [objectId]. A missing description needs a validity probe to distinguish absence from an unreadable parent.
 * `get_instance_id()` fails for a null parent. `is_instance_valid()` accepts null.
 * Probe only after a missing result or an unreadable descriptor.
 * A failed request skips the probe because another request cannot clarify a transport error or timeout.
 */
private suspend fun DapSessionContext.resolveParent(evaluator: GdSelfCheckedFrame, objectId: Long): GdNodeResolution {
    val describeExpr = GdDapExpressions.describe(GdDapExpressions.parent(objectId))
    val describeOutcome = evaluateStructuredOutcome(evaluator, describeExpr)
    val descriptor = when (describeOutcome) {
        is GdEvalOutcome.Structured -> {
            val variables = when (val load = loadVariablesBounded(describeOutcome.variable)) {
                is GdVariablesLoad.Success -> load.variables
                is GdVariablesLoad.Failed -> {
                    LOG.warn("Could not describe parent of #$objectId, skipping the validity probe: ${load.message}")
                    return GdNodeResolution.Absent(GdScriptBundle.message("gdscript.debugger.node.parent.unavailable"))
                }
            }
            GdArrayReader(variables).readDescriptors(0, count = 1).firstOrNull()
        }

        is GdEvalOutcome.Missing, is GdEvalOutcome.Failed -> null
    }
    if (descriptor != null) return GdNodeResolution.Resolved(descriptor, variable = null)

    if (describeOutcome is GdEvalOutcome.Failed) {
        if (describeOutcome.kind != GdEvalOutcome.Failed.Kind.RESPONSE_MISMATCH)
            LOG.warn("Could not describe parent of #$objectId, skipping the validity probe: ${describeOutcome.message}")
        return GdNodeResolution.Absent(GdScriptBundle.message("gdscript.debugger.node.parent.unavailable"))
    }

    val validOutcome = evaluateStructuredOutcome(evaluator, GdDapExpressions.parentValid(objectId))
    val valid = when (validOutcome) {
        is GdEvalOutcome.Structured -> {
            val variables = when (val load = loadVariablesBounded(validOutcome.variable)) {
                is GdVariablesLoad.Success -> load.variables
                is GdVariablesLoad.Failed -> {
                    LOG.warn("Could not check parent validity for #$objectId: ${load.message}")
                    return GdNodeResolution.Absent(GdScriptBundle.message("gdscript.debugger.node.parent.unavailable"))
                }
            }
            GdArrayReader(variables).getOrNull(0)?.value
        }

        is GdEvalOutcome.Missing, is GdEvalOutcome.Failed -> null
    }

    return when (valid) {
        "false" -> GdNodeResolution.Absent("null")
        // A true or unreadable answer leaves the parent unavailable.
        else -> GdNodeResolution.Absent(GdScriptBundle.message("gdscript.debugger.node.parent.unavailable"))
    }
}
