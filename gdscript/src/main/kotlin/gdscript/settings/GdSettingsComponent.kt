package gdscript.settings

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

class GdSettingsComponent(val project: Project) {
    val panel: JPanel

    private val hidePrivateCheck = JBCheckBox("Hide _private members from completion")
    private val shortTypedCheck = JBCheckBox("Use short typing 'var a := 1' instead of 'var a: int = 1'")
    private val annotatorsCb = ComboBox<String>()
    private val criticalsTf: JTextField = JTextField()
    private val warningsTf: JTextField = JTextField()
    private val notesTf: JTextField = JTextField()

    init {
        annotatorsCb.addItem(GdProjectState.OFF)
        annotatorsCb.addItem(GdProjectState.WARN)
        annotatorsCb.addItem(GdProjectState.ERR)
        annotatorsCb.selectedItem = GdProjectState.OFF

        panel = FormBuilder.createFormBuilder()
                .addComponent(hidePrivateCheck, 1)
                .addComponent(shortTypedCheck, 1)
                .addLabeledComponent("Reference, Node, Resource checks", annotatorsCb, 1)
                .addLabeledComponent("Critical comments", criticalsTf, 1)
                .addLabeledComponent("Warning comments", warningsTf, 1)
                .addLabeledComponent("Note comments", notesTf, 1)
                .addComponentFillVertically(JPanel(), 0)
                .panel
    }

    fun preferredFocusedComponent(): JComponent = hidePrivateCheck

    var hidePrivate: Boolean
        get() = hidePrivateCheck.isSelected
        set(newStatus) {
            hidePrivateCheck.isSelected = newStatus
        }

    var shortTyped: Boolean
        get() = shortTypedCheck.isSelected
        set(newStatus) {
            shortTypedCheck.isSelected = newStatus
        }

    var annotators: String
        get() = annotatorsCb.selectedItem as String
        set(newStatus) {
            annotatorsCb.selectedItem = newStatus
        }

    var criticals: String
        get() = criticalsTf.text
        set(newStatus) {
            criticalsTf.text = newStatus
        }

    var warnings: String
        get() = warningsTf.text
        set(newStatus) {
            warningsTf.text = newStatus
        }

    var notes: String
        get() = notesTf.text
        set(newStatus) {
            notesTf.text = newStatus
        }

}
