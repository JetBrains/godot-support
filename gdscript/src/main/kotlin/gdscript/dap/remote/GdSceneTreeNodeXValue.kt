package gdscript.dap.remote

import com.intellij.diagnostic.rethrowControlFlowException
import com.intellij.icons.AllIcons
import com.intellij.openapi.diagnostic.logger
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.DapSessionStoppedException
import com.intellij.platform.dap.xdebugger.DapXDebuggerPresentationFactory
import com.intellij.xdebugger.XExpression
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XNamedValue
import com.intellij.xdebugger.frame.XValueGroup
import com.intellij.xdebugger.frame.XValueNode
import com.intellij.xdebugger.frame.XValuePlace
import com.jetbrains.dap.protocol.DapConnectionClosedException
import gdscript.GdScriptBundle
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import org.jetbrains.concurrency.Promise

private val LOG = logger<GdSceneTreeNodeXValue>()

/** One root outcome shared by presentation, expansion, and the watch expression. */
private sealed interface GdRootResolution {
    data class Resolved(val descriptor: GdNodeDescriptor) : GdRootResolution

    sealed interface Unresolved : GdRootResolution {
        val message: String
    }

    data object Missing : Unresolved {
        override val message: String get() = GdScriptBundle.message("gdscript.debugger.scene.tree.error.node.not.found")
    }

    data class Failed(val reason: String, val cause: Throwable? = null) : Unresolved {
        override val message: String get() = GdScriptBundle.message("gdscript.debugger.scene.tree.error.evaluation.failed", reason)
    }
}

/** A scene tree row with inline children and a Properties group below them. See [GdNodeMembersXValue] for other node rows. */
internal class GdSceneTreeNodeXValue private constructor(
    name: String,
    private val executor: DapSessionExecutor,
    private val evaluator: GdSelfCheckedFrame,
    private val factory: DapXDebuggerPresentationFactory,
    private val resolutionDeferred: Deferred<GdRootResolution>,
) : XNamedValue(name) {

    /** Uses a known descriptor. Properties resolve by ObjectID on expansion. */
    constructor(
        descriptor: GdNodeDescriptor,
        executor: DapSessionExecutor,
        evaluator: GdSelfCheckedFrame,
        factory: DapXDebuggerPresentationFactory
    ) : this(
        name = descriptor.nodeName ?: descriptor.className,
        executor = executor,
        evaluator = evaluator,
        factory = factory,
        resolutionDeferred = CompletableDeferred(GdRootResolution.Resolved(descriptor))
    )

    /** Starts root resolution at construction. Only a missing primary result permits the fallback. */
    constructor(
        name: String,
        executor: DapSessionExecutor,
        evaluator: GdSelfCheckedFrame,
        factory: DapXDebuggerPresentationFactory
    ) : this(
        name = name,
        executor = executor,
        evaluator = evaluator,
        factory = factory,
        resolutionDeferred = executor.postAsync { resolveRootDescriptor(evaluator) }
    )

    override fun computePresentation(node: XValueNode, place: XValuePlace) {
        executor.computePresentationWithCompletion(node, LOG, "scene tree node") { target ->
            when (val resolution = resolutionDeferred.await()) {
                is GdRootResolution.Resolved -> {
                    val row = GdNodePresentation.byClassName(resolution.descriptor)
                    target.setPresentation(row.icon, row.presentation, true)
                }
                is GdRootResolution.Unresolved -> {
                    when (resolution) {
                        is GdRootResolution.Failed -> {
                            val cause = resolution.cause
                            if (cause is DapSessionStoppedException || cause is DapConnectionClosedException) throw cause
                        }
                        GdRootResolution.Missing -> Unit
                    }
                    target.setPresentation(null, null, resolution.message, false)
                }
            }
        }
    }

    override fun computeChildren(node: XCompositeNode) {
        executor.computeChildrenWithCompletion(
            node = node,
            log = LOG,
            subject = "scene tree children",
            errorMessage = ::childLoadErrorMessage,
        ) { target ->
            when (val resolution = resolutionDeferred.await()) {
                is GdRootResolution.Resolved -> addChildRows(
                    target, evaluator, resolution.descriptor.objectId, executor, factory,
                    sceneTree = true, bottomGroup = createPropertiesGroup(resolution.descriptor)
                )
                is GdRootResolution.Unresolved -> target.setErrorMessage(resolution.message)
            }
        }
    }

    /** Resolves the DAP variable by ObjectID only when the user opens Properties. */
    private fun createPropertiesGroup(descriptor: GdNodeDescriptor): XValueGroup {
        return object : XValueGroup("Properties") {
            override fun getIcon(): javax.swing.Icon = AllIcons.Nodes.Property

            override fun computeChildren(node: XCompositeNode) {
                executor.computeChildrenWithCompletion(
                    node = node,
                    log = LOG,
                    subject = "properties",
                    errorMessage = {
                        GdScriptBundle.message("gdscript.debugger.scene.tree.error.properties.failed",
                            it.message ?: GdScriptBundle.message("gdscript.debugger.error.unknown"))
                    },
                ) { target ->
                    when (val properties = loadNodeProperties(executor, evaluator, factory, descriptor.objectId, knownVariable = null)) {
                        is GdPropertiesLoad.Success -> {
                            target.setAlreadySorted(true)
                            target.addChildren(properties.properties, true)
                        }
                        is GdPropertiesLoad.Failure -> target.setErrorMessage(properties.message)
                    }
                }
            }
        }
    }

    override fun getEvaluationExpression(): String? = null

    override fun calculateEvaluationExpression(): Promise<XExpression> = executor.computeNodeEvaluationExpression { reject ->
        when (val resolution = resolutionDeferred.await()) {
            is GdRootResolution.Resolved -> resolution.descriptor.objectId
            is GdRootResolution.Unresolved -> {
                reject(resolution.message)
                null
            }
        }
    }
}

private suspend fun DapSessionContext.resolveRootDescriptor(evaluator: GdSelfCheckedFrame): GdRootResolution {
    suspend fun resolve(expression: String): GdRootResolution = when (
        val outcome = evaluateStructuredOutcome(evaluator, GdDapExpressions.describe(expression))
    ) {
        is GdEvalOutcome.Failed -> GdRootResolution.Failed(outcome.message)
        GdEvalOutcome.Missing -> GdRootResolution.Missing
        is GdEvalOutcome.Structured -> when (val load = loadVariablesBounded(outcome.variable)) {
            is GdVariablesLoad.Failed -> GdRootResolution.Failed(load.message)
            is GdVariablesLoad.Success -> {
                val descriptor = GdArrayReader(load.variables).readDescriptors(0, count = 1).firstOrNull()
                if (descriptor != null) GdRootResolution.Resolved(descriptor)
                else GdRootResolution.Failed(GdScriptBundle.message("gdscript.debugger.error.unknown"))
            }
        }
    }

    try {
        return when (val primary = resolve(GdDapExpressions.ROOT)) {
            is GdRootResolution.Resolved -> primary
            is GdRootResolution.Unresolved -> when (primary) {
                is GdRootResolution.Failed -> primary
                GdRootResolution.Missing -> resolve(GdDapExpressions.ROOT_FALLBACK)
            }
        }
    }
    catch (e: Throwable) {
        rethrowControlFlowException(e)
        if (e !is DapSessionStoppedException && e !is DapConnectionClosedException) {
            LOG.warn("Could not resolve scene tree node presentation", e)
        }
        return GdRootResolution.Failed(e.message ?: GdScriptBundle.message("gdscript.debugger.error.unknown"), e)
    }
}
