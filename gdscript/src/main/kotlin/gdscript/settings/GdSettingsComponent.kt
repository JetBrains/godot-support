package gdscript.settings

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

class GdSettingsComponent {
    val panel: JPanel

    private val hidePrivateCheck = JBCheckBox("Hide _private members from completion")
    private val shortTypedCheck = JBCheckBox("Use short typing 'var a := 1' instead of 'var a: int = 1'")
    private val shouldTypeCheck = JBCheckBox("Enable variable not typed warning")
    private val descriptor: FileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
    private val sdkField = JTextField("GdScript SDK")
    private val annotatorsCb = ComboBox<String>()
    private val sdk = TextFieldWithBrowseButton(sdkField) {
        val chosen = FileChooser.chooseFile(descriptor, null, null)
        if (chosen != null) {
            val f = VfsUtilCore.virtualToIoFile(chosen)
            sdkField.text = FileUtilRt.toSystemDependentName(f.path)
        }
    }

    init {
        sdk.toolTipText
        annotatorsCb.addItem(GdState.OFF)
        annotatorsCb.addItem(GdState.WARN)
        annotatorsCb.addItem(GdState.ERR)
        annotatorsCb.selectedItem= GdState.OFF

        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent("GdScript SDK", sdk, 1)
            .addComponent(hidePrivateCheck, 1)
            .addComponent(shouldTypeCheck, 1)
            .addComponent(shortTypedCheck, 1)
            .addLabeledComponent("Reference, Node, Resource checks", annotatorsCb, 1)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun preferredFocusedComponent(): JComponent = hidePrivateCheck

    var hidePrivate: Boolean
        get() = hidePrivateCheck.isSelected
        set(newStatus) {
            hidePrivateCheck.isSelected = newStatus
        }

    var sdkPath: String?
        get() = sdkField.text
        set(path) {
            sdkField.text = path.orEmpty()
        }

    var shortTyped: Boolean
        get() = shortTypedCheck.isSelected
        set(newStatus) {
            shortTypedCheck.isSelected = newStatus
        }

    var shouldType: Boolean
        get() = shouldTypeCheck.isSelected
        set(newStatus) {
            shouldTypeCheck.isSelected = newStatus
        }

    var annotators: String
        get() = annotatorsCb.selectedItem as String
        set(newStatus) {
            annotatorsCb.selectedItem = newStatus
        }

}
