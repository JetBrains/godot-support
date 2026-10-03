package gdscript.dap.remote

import com.intellij.icons.AllIcons
import com.intellij.openapi.diagnostic.logger
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.xdebugger.DapXDebuggerPresentationFactory
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XValueGroup
import gdscript.GdScriptBundle
import javax.swing.Icon

private val LOG = logger<GdChildrenXValueGroup>()

/**
 * Shows direct children in Godot sibling order.
 * The label has no count because the count requires evaluation on expansion. An empty node still has this group.
 */
internal class GdChildrenXValueGroup(
    private val parentObjectId: Long,
    private val executor: DapSessionExecutor,
    private val evaluator: GdSelfCheckedFrame,
    private val factory: DapXDebuggerPresentationFactory,
) : XValueGroup("Children") {

    override fun getIcon(): Icon = AllIcons.Hierarchy.Subtypes

    override fun computeChildren(node: XCompositeNode) {
        executor.computeChildrenWithCompletion(
            node = node,
            log = LOG,
            subject = "child nodes",
            errorMessage = ::childLoadErrorMessage,
        ) { target ->
            addChildRows(target, evaluator, parentObjectId, executor, factory)
        }
    }
}
