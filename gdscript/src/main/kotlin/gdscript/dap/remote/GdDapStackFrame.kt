package gdscript.dap.remote

import com.intellij.icons.AllIcons
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.platform.dap.DapEvaluationResult
import com.intellij.platform.dap.DapInlineValueLocator
import com.intellij.platform.dap.DapScope
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.DapThread
import com.intellij.platform.dap.DapThreadState
import com.intellij.platform.dap.DapVariable
import com.intellij.platform.dap.xdebugger.DapXDebuggerPresentationFactory
import com.intellij.platform.dap.xdebugger.DefaultDapXStackFrame
import com.intellij.ui.SimpleTextAttributes
import com.intellij.xdebugger.XDebuggerBundle
import com.intellij.xdebugger.XSourcePosition
import com.intellij.xdebugger.evaluation.ExpressionInfo
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XValueChildrenList
import gdscript.GdScriptBundle
import org.jetbrains.concurrency.Promise

private val LOG = logger<GdDapStackFrame>()

internal class GdDapStackFrame(
    factory: DapXDebuggerPresentationFactory,
    executor: DapSessionExecutor,
    thread: DapThread,
    private val checkedFrame: GdSelfCheckedFrame,
    private val inlineValueLocator: DapInlineValueLocator? = null,
) : DefaultDapXStackFrame(factory, executor, thread, checkedFrame) {

    /**
     * The platform restores tree expansion when the next frame has the same key.
     * Recursive calls and anonymous lambdas with the same source and name share a key.
     */
    override fun getEqualityObject(): Any = "${thread.id}:${frame.source?.path}:${frame.name}"

    /** Supplies the hover range. [gdEvaluator] sends each evaluation through the self check. */
    private val platformEvaluator: XDebuggerEvaluator by lazy { super.getEvaluator() }
    private val gdEvaluator: XDebuggerEvaluator by lazy { GdFrameEvaluator() }

    override fun getEvaluator(): XDebuggerEvaluator = gdEvaluator

    private inner class GdFrameEvaluator : XDebuggerEvaluator() {
        override fun getExpressionInfoAtOffsetAsync(
            project: Project,
            document: Document,
            offset: Int,
            sideEffectsAllowed: Boolean,
        ): Promise<ExpressionInfo?> = platformEvaluator.getExpressionInfoAtOffsetAsync(project, document, offset, sideEffectsAllowed)

        override fun evaluate(expression: String, callback: XEvaluationCallback, expressionPosition: XSourcePosition?) {
            // Keep watch text unchanged. Store the expression only on this result row.
            executor.evaluateWithCompletion(callback) { answer ->
                when (val result = checkedFrame.run { evaluate(expression) }) {
                    is DapEvaluationResult.Error -> answer.errorOccurred(result.errorMessage)
                    is DapEvaluationResult.Success -> {
                        val owner = GdOwnerExpression(expression)
                        val row = GdDapXValue(factory, executor, result.variable, owner = owner, frame = checkedFrame)
                        answer.evaluated(row)
                        // Show the plain result before the optional probe updates its presentation.
                        if (GdObjectProbe.isProbeTarget(result.variable, owner, allowInferredType = true) &&
                            hasSelfInMembers(checkedFrame)) {
                            val presentations = with(GdObjectProbe) { load(checkedFrame, listOf(expression)) }
                            row.updatePresentation(presentations?.singleOrNull())
                        }
                    }
                }
            }
        }
    }

    /**
     * Each call fills its own [node]. A later call does not cancel an earlier call.
     * [XCompositeNode.isObsolete] identifies a consumer that no longer needs rows.
     */
    override fun computeChildren(node: XCompositeNode) {
        // Match the thread check in DefaultDapXStackFrame.computeChildren.
        val threadState = thread.state
        if (threadState !is DapThreadState.Paused) {
            try {
                node.setErrorMessage(XDebuggerBundle.message("debugger.frames.dialog.message.not.available.for.unsuspended"))
            }
            finally {
                node.addChildren(XValueChildrenList.EMPTY, true)
            }
            return
        }

        executor.computeChildrenWithCompletion(
            node = node,
            log = LOG,
            subject = "stack frame children",
            errorMessage = { GdScriptBundle.message("gdscript.debugger.error.unknown") },
        ) { target ->
            val rowData = gatherRowData(threadState)
            emitRows(target, rowData)
        }
    }

    /** Completes all suspending reads before it emits any rows. */
    private suspend fun DapSessionContext.gatherRowData(threadState: DapThreadState.Paused): RowData {
        // 1. Check the source and the dump before enabling root and self rows.
        val selfVariable = when (val self = checkedFrame.run { checkSelf() }) {
            is GdFrameSelf.Present -> self.variable
            GdFrameSelf.Absent, GdFrameSelf.Unavailable, GdFrameSelf.TimedOut -> null
        }

        // 2. Read the exception value, or keep its message if the value is unavailable.
        val exceptionInfo = threadState.exceptionInfo
        val exceptionGetterExpression = exceptionInfo?.rawDetails?.evaluateName
        val exceptionEvaluation = if (selfVariable == null) null
        else exceptionGetterExpression?.let { checkedFrame.run { evaluate(it) } }
        val exceptionVariable = (exceptionEvaluation as? DapEvaluationResult.Success)?.variable
        val exceptionMessage = if (exceptionVariable == null) exceptionInfo?.message else null

        // 3. Detect whether self is a Node.
        val detection = if (selfVariable != null) detectFrameNode(checkedFrame) else GdNodeDetection.NonNode
        val frameNode: GdNodeDescriptor?
        val detectionError: String?
        when (detection) {
            is GdNodeDetection.Node -> {
                frameNode = detection.node
                detectionError = null
            }
            GdNodeDetection.NonNode -> {
                frameNode = null
                detectionError = null
            }
            is GdNodeDetection.Failed -> {
                frameNode = null
                LOG.warn("Could not inspect the frame base instance: ${detection.message}")
                detectionError = if (exceptionMessage != null || exceptionVariable != null) null
                else GdScriptBundle.message("gdscript.debugger.error.evaluation.failed", detection.message)
            }
        }

        // 4. Read the scopes.
        val scopes = checkedFrame.run { scopes() }
        return RowData(exceptionVariable, exceptionMessage, selfVariable, frameNode, scopes, detectionError)
    }

    /**
     * Emits rows without suspension after it checks whether the consumer is obsolete.
     * `XValueContainerNode.createNodes` places top values before top groups within each batch.
     * Separate batches keep the required order in this frame and in [GdNodeMembersXValue].
     * [computeChildrenWithCompletion] completes the consumer, also if emission fails.
     */
    private fun emitRows(target: XCompositeNode, rowData: RowData) {
        if (target.isObsolete) {
            LOG.trace("Skipping emission because the node became obsolete during the reads")
            return
        }

        // 1. Show the exception information.
        if (rowData.exceptionVariable != null) {
            target.addChildren(XValueChildrenList.singleton(
                factory.createValue(executor, rowData.exceptionVariable, AllIcons.Nodes.ExceptionClass)
            ), false)
        }
        else if (rowData.exceptionMessage != null) {
            target.setMessage(rowData.exceptionMessage, AllIcons.Nodes.ExceptionClass, SimpleTextAttributes.ERROR_ATTRIBUTES, null)
        }

        // 2. Show the root and self rows when the self check permits them.
        if (rowData.selfVariable != null) {
            val rootBatch = XValueChildrenList()
            rootBatch.addTopValue(GdSceneTreeNodeXValue(
                name = "root", // Not localizable
                executor = executor,
                evaluator = checkedFrame,
                factory = factory
            ))
            target.addChildren(rootBatch, false)

            val selfBatch = XValueChildrenList()
            if (rowData.frameNode != null) {
                selfBatch.addTopValue(GdNodeMembersXValue.selfOf(
                    objectId = rowData.frameNode.objectId,
                    nodeName = rowData.frameNode.nodeName,
                    className = rowData.frameNode.className,
                    selfVariable = rowData.selfVariable,
                    executor = executor,
                    evaluator = checkedFrame,
                    factory = factory
                ))
            }
            else {
                selfBatch.addTopValue(GdDapXValue(factory, executor, rowData.selfVariable,
                    owner = GdOwnerExpression("self"), frame = checkedFrame))
            }
            target.addChildren(selfBatch, false)
        }

        rowData.detectionError?.let { target.setErrorMessage(it) }

        // 3. The self and root rows replace Members and Globals. Keep each original scope index.
        val gateActive = rowData.selfVariable != null
        for ((index, scope) in rowData.scopes.withIndex()) {
            if (gateActive && scope.name == GdSelfCheckPolicy.MEMBERS_SCOPE_NAME) continue
            if (gateActive && scope.name == GdSelfCheckPolicy.GLOBALS_SCOPE_NAME) continue

            val scopeBatch = XValueChildrenList()
            // Locals with self can use their names as owner expressions. Other scopes keep platform values.
            scopeBatch.addTopGroup(if (gateActive && scope.name == "Locals")
                GdDapXScope(factory, executor, scope, index, checkedFrame, inlineValueLocator)
            else factory.createScope(executor, scope, index))
            target.addChildren(scopeBatch, false)
        }
    }

    /** The complete snapshot used by [emitRows]. */
    private data class RowData(
        val exceptionVariable: DapVariable?,
        val exceptionMessage: String?,
        val selfVariable: DapVariable?,
        val frameNode: GdNodeDescriptor?,
        val scopes: List<DapScope>,
        val detectionError: String?,
    )
}
