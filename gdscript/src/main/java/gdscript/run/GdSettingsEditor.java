package gdscript.run;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import javax.swing.*;

public class GdSettingsEditor extends SettingsEditor<GdRunConfiguration> {

    private JPanel myPanel;
    private LabeledComponent<TextFieldWithBrowseButton> godotExe;
    private LabeledComponent<TextFieldWithBrowseButton> tscn;

    public void resetEditorFrom(GdRunConfiguration gdRunConfiguration) {
        godotExe.getComponent().setText(gdRunConfiguration.getGodotExe());
        tscn.getComponent().setText(gdRunConfiguration.getTscn());
    }

    public void applyEditorTo(GdRunConfiguration gdRunConfiguration) {
        gdRunConfiguration.setGodotExe(godotExe.getComponent().getText());
        gdRunConfiguration.setTscn(tscn.getComponent().getText());
    }

    public JComponent createEditor() {
        return myPanel;
    }

    private void createUIComponents() {
        TextFieldWithBrowseButton godotField = new TextFieldWithBrowseButton();
        godotField.setText("Godot.exe");
        godotField.setToolTipText("Godot.exe");
        godotField.addBrowseFolderListener(new TextBrowseFolderListener(
                FileChooserDescriptorFactory.createSingleFileDescriptor("exe")
        ));
        godotExe.setComponent(godotField);

        TextFieldWithBrowseButton tscnField = new TextFieldWithBrowseButton();
        tscnField.setText("Node.tscn");
        tscnField.setToolTipText("Node.tscn");
        tscnField.addBrowseFolderListener(new TextBrowseFolderListener(
                FileChooserDescriptorFactory.createSingleFileDescriptor("tscn")
        ));
        tscn.setComponent(tscnField);
    }

}
