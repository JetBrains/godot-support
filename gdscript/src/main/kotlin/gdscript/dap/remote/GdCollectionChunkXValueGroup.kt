package gdscript.dap.remote

import com.intellij.openapi.diagnostic.logger
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XValueChildrenList
import com.intellij.xdebugger.frame.XValueGroup
import gdscript.GdScriptBundle

private val LOG = logger<GdCollectionChunkXValueGroup>()

/**
 * Opens a range without reloading the collection variables.
 * Row construction can request object presentations.
 */
internal class GdCollectionChunkXValueGroup(
    private val range: GdChildRange,
    private val executor: DapSessionExecutor,
    private val children: suspend DapSessionContext.(GdChildRange) -> XValueChildrenList,
) : XValueGroup(range.label) {
    override fun computeChildren(node: XCompositeNode) {
        executor.computeChildrenWithCompletion(
            node = node,
            log = LOG,
            subject = "collection chunk",
            errorMessage = { GdScriptBundle.message("gdscript.debugger.error.unknown") },
        ) { target ->
            if (!target.isObsolete) {
                val rows = children(range)
                if (!target.isObsolete) target.addChildren(rows, true)
            }
        }
    }
}
