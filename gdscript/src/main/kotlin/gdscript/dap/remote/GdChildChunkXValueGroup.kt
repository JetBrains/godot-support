package gdscript.dap.remote

import com.intellij.openapi.diagnostic.logger
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.xdebugger.DapXDebuggerPresentationFactory
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XValueGroup

private val LOG = logger<GdChildChunkXValueGroup>()

/** Expands only its absolute range. A range that contains more groups needs no evaluation. */
internal class GdChildChunkXValueGroup(
    private val range: GdChildRange,
    private val parentObjectId: Long,
    private val executor: DapSessionExecutor,
    private val evaluator: GdSelfCheckedFrame,
    private val factory: DapXDebuggerPresentationFactory,
    private val sceneTree: Boolean,
) : XValueGroup(range.label) {

    override fun computeChildren(node: XCompositeNode) {
        executor.computeChildrenWithCompletion(
            node = node,
            log = LOG,
            subject = "child chunk",
            errorMessage = ::childLoadErrorMessage,
        ) { target ->
            addChildRows(target, evaluator, parentObjectId, executor, factory, sceneTree, range)
        }
    }
}
