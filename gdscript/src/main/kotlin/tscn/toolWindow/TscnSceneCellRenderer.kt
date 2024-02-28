package tscn.toolWindow

import com.intellij.ui.JBColor
import gdscript.GdIcon
import tscn.toolWindow.model.TscnSceneTreeNode
import java.awt.BorderLayout
import java.awt.Component
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
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
        val panel = JPanel(BorderLayout())
        panel.add(this, BorderLayout.WEST)

        if (value is TscnSceneTreeNode) {
            text = value.myName
            icon = GdIcon.getEditorIcon(value.myType)
            if (value.inherited) foreground = JBColor.YELLOW

            if (value.hasScript || value.hasUniqueName) {
                val icoPanel = JPanel()
                icoPanel.border = BorderFactory.createEmptyBorder(0, 0, 0, 5)

                if (value.hasScript) icoPanel.add(JLabel(GdIcon.getEditorIcon("Script")))
                if (value.hasUniqueName) icoPanel.add(JLabel(GdIcon.getEditorIcon("SceneUniqueName")))

                panel.add(icoPanel, BorderLayout.EAST)
            }
        }

        return panel
    }

}
