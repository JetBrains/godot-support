package gdscript.run;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.SystemInfo;

import javax.swing.*;

public class GdSettingsEditor extends SettingsEditor<GdRunConfiguration> {

    private JPanel myPanel;
    private LabeledComponent<TextFieldWithBrowseButton> godotExe;
    private LabeledComponent<TextFieldWithBrowseButton> tscn;
    private JCheckBox debugShapes;

    public void resetEditorFrom(GdRunConfiguration gdRunConfiguration) {
        godotExe.getComponent().setText(gdRunConfiguration.getGodotExe());
        tscn.getComponent().setText(gdRunConfiguration.getTscn());
        debugShapes.setSelected(gdRunConfiguration.isDebugShapes());
    }

    public void applyEditorTo(GdRunConfiguration gdRunConfiguration) {
        gdRunConfiguration.setGodotExe(godotExe.getComponent().getText());
        gdRunConfiguration.setTscn(tscn.getComponent().getText());
        gdRunConfiguration.setDebugShapes(debugShapes.isSelected());
    }

    public JComponent createEditor() {
        return myPanel;
    }

    private void createUIComponents() {
        godotExe = new LabeledComponent<>();
        tscn = new LabeledComponent<>();
        TextFieldWithBrowseButton godotField = new TextFieldWithBrowseButton();
        godotField.setText("");
        godotField.setToolTipText("Path to the Godot Executable");
        godotField.addBrowseFolderListener(new TextBrowseFolderListener(
                SystemInfo.isWindows ?
                        FileChooserDescriptorFactory.createSingleFileDescriptor("exe") :
                        FileChooserDescriptorFactory.createSingleFileOrExecutableAppDescriptor()
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
