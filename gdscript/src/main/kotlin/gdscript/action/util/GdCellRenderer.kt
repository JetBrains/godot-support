package gdscript.action.util

import com.intellij.openapi.editor.colors.EditorFontType
import java.awt.BorderLayout
import java.awt.Component
import javax.swing.*

object GdCellRenderer : ListCellRenderer<GdTraitClass> {

    private val myPanel: JPanel
    private val myNameLabel: JLabel

    init {
        myPanel = JPanel(BorderLayout())
        myPanel.border = BorderFactory.createEmptyBorder(0, 2, 0, 0)
        myNameLabel = JLabel()
        myPanel.add(myNameLabel, BorderLayout.CENTER)
        val font = EditorFontType.getGlobalPlainFont()
        myNameLabel.font = font
    }

    override fun getListCellRendererComponent(
        list: JList<out GdTraitClass>,
        value: GdTraitClass,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean,
    ): Component {
        val backgroundColor = if (isSelected) list.selectionBackground else list.background
        myNameLabel.text = value.name
        myNameLabel.foreground = if (isSelected) list.selectionForeground else list.foreground
        myPanel.background = backgroundColor
        return myPanel
    }

}
