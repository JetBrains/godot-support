package tscn.toolWindow

import com.intellij.ui.JBColor
import gdscript.GdIcon
import tscn.toolWindow.model.TscnSceneTreeNode
import java.awt.Component
import javax.swing.JTree
import javax.swing.tree.DefaultTreeCellRenderer

class TscnSceneCellRenderer : DefaultTreeCellRenderer() {

    override fun getTreeCellRendererComponent(
        tree: JTree,
        value: Any,
        selected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean,
    ): Component {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus)
        if (value is TscnSceneTreeNode) {
            text = value.myName
            icon = GdIcon.getEditorIcon(value.myType)
            if (value.inherited) foreground = JBColor.YELLOW
        }

        return this
    }

}
