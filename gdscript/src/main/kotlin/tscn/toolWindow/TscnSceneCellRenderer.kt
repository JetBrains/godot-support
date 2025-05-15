package tscn.toolWindow

import GdScriptPluginIcons
import com.intellij.openapi.project.Project
import com.intellij.ui.JBColor
import gdscript.GdIcon
import tscn.toolWindow.model.TscnSceneTreeNode
import java.awt.*
import javax.swing.BorderFactory
import javax.swing.Icon
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTree
import javax.swing.tree.DefaultTreeCellRenderer

class TscnSceneCellRenderer : DefaultTreeCellRenderer {

    companion object {
        val BUTTON_WIDTH = 16
    }

    val project: Project

    constructor(project: Project) {
        this.project = project
    }

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

            val icons = listIcons(value)
            if (icons.isNotEmpty()) {
                val icoPanel = JPanel()
                icoPanel.border = BorderFactory.createEmptyBorder(0, 0, 0, 5)
                icoPanel.layout = GridBagLayout()
                val gbc = GridBagConstraints()
                icons.forEach { icoPanel.add(getComponent(it), gbc) }
                panel.add(icoPanel, BorderLayout.EAST)
            }
        }

        return panel
    }

    private fun listIcons(node: TscnSceneTreeNode): List<Icon> {
        return node.listActions().mapNotNull {
            when (it) {
                "instance" -> GdScriptPluginIcons.TscnIcons.InstanceOptions
                "script" -> GdScriptPluginIcons.TscnIcons.Script
                "unique" -> GdScriptPluginIcons.TscnIcons.SceneUniqueName
                "visible" -> if (node.visible){
                    if (node.parentVisible)
                        GdScriptPluginIcons.TscnIcons.GuiVisibilityVisible
                    else
                        GdScriptPluginIcons.TscnIcons.GuiVisibilityVisibleDark
                }
                else if (node.parentVisible)
                    GdScriptPluginIcons.TscnIcons.GuiVisibilityHidden
                else
                    GdScriptPluginIcons.TscnIcons.GuiVisibilityHiddenDark
                else -> null
            }
        }
    }

    private fun getComponent(icon: Icon): Component {
        val label = JLabel(icon)
        val dim = Dimension(BUTTON_WIDTH, 16)
        label.size = dim
        label.preferredSize = dim

        return label
    }
}
