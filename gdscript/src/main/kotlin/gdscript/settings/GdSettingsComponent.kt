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
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

class GdSettingsComponent {
    val panel: JPanel

    private val hidePrivateCheck = JBCheckBox("Hide _private members from completion")
    private val descriptor: FileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
    private val sdkField = JTextField("GdScript SDK");
    private val colorTypeCB = ComboBox<String>()
    private val sdk = TextFieldWithBrowseButton(sdkField) {
        val chosen = FileChooser.chooseFile(descriptor, null, null);
        if (chosen != null) {
            val f = VfsUtilCore.virtualToIoFile(chosen);
            sdkField.text = FileUtilRt.toSystemDependentName(f.path);
        }
    }

    init {
        sdk.toolTipText
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent("GdScript SDK", sdk, 1)
            .addComponent(hidePrivateCheck, 1)
            .addComponentFillVertically(JPanel(), 0)
            .panel
        colorTypeCB.toolTipText = "Color format"
        colorTypeCB.addItem(GdSettings.COLOR_FORMAT_DEFAULT)
        colorTypeCB.addItem(GdSettings.COLOR_FORMAT_HEXA)
        colorTypeCB.addItem(GdSettings.COLOR_FORMAT_8BIT)
    }

    fun preferredFocusedComponent(): JComponent = hidePrivateCheck;

    var hidePrivate: Boolean
        get() = hidePrivateCheck.isSelected
        set(newStatus) {
            hidePrivateCheck.isSelected = newStatus
        }

    var sdkPath: String?
        get() = sdkField.text;
        set(path) {
            sdkField.text = path.orEmpty();
        }

    var colorFormat: String
        get() = colorTypeCB.selectedItem as String
        set(newType) {
            colorTypeCB.selectedItem = newType
        }

}
