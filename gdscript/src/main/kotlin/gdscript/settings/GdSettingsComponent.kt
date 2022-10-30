package gdscript.settings

import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class GdSettingsComponent {
    val panel: JPanel

    private val hidePrivateCheck = JBCheckBox("Hide _private members from completion")

    init {
        panel = FormBuilder.createFormBuilder()
            .addComponent(hidePrivateCheck, 1)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun preferredFocusedComponent(): JComponent = hidePrivateCheck;

    var hidePrivate: Boolean
        get() = hidePrivateCheck.isSelected
        set(newStatus) {
            hidePrivateCheck.isSelected = newStatus
        }

}
