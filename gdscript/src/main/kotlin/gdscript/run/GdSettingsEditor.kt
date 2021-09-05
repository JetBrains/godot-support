package gdscript.run

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import javax.swing.*;

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
        godotExe.setComponent(TextFieldWithBrowseButton());
        tscn = LabeledComponent();
        tscn.setComponent(TextFieldWithBrowseButton());
    }

}
