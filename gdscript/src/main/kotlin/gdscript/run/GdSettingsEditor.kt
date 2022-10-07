package gdscript.run

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import javax.swing.*

class GdSettingsEditor : SettingsEditor<GdRunConfiguration>() {

    lateinit var myPanel: JPanel;
    lateinit var godotExe: LabeledComponent<TextFieldWithBrowseButton>;
    lateinit var tscn: LabeledComponent<TextFieldWithBrowseButton>;

    override fun resetEditorFrom(gdRunConfiguration: GdRunConfiguration) {
        godotExe.getComponent().setText(gdRunConfiguration.getGodotExe());
        tscn.getComponent().setText(gdRunConfiguration.getTscn());
    }

    override fun applyEditorTo(gdRunConfiguration: GdRunConfiguration) {
        gdRunConfiguration.setGodotExe(godotExe.getComponent().getText());
        gdRunConfiguration.setTscn(tscn.getComponent().getText());
    }

    override fun createEditor(): JComponent = myPanel;

    private fun createUIComponents() {
        godotExe = LabeledComponent();
        val godotField = TextFieldWithBrowseButton()
        godotField.text = "Godot.exe"
        godotField.toolTipText = "Godot.exe"
        godotField.addBrowseFolderListener(TextBrowseFolderListener(FileChooserDescriptor(true,
            false,
            false,
            false,
            false,
            false)));
        godotExe.setComponent(godotField);

        tscn = LabeledComponent();
        val tscnField = TextFieldWithBrowseButton()
        tscnField.text = "Node.tscn"
        tscnField.toolTipText = "Node.tscn"
        tscnField.addBrowseFolderListener(TextBrowseFolderListener(FileChooserDescriptor(true,
            false,
            false,
            false,
            false,
            false)));
        tscn.setComponent(tscnField);
    }

}
