package gdscript.run;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;
import gdscript.GdScriptBundle;

import javax.swing.*;

public class GdSettingsEditor extends SettingsEditor<GdRunConfiguration> {

    private JPanel myPanel;
    private LabeledComponent<TextFieldWithBrowseButton> godotExe;
    private LabeledComponent<TextFieldWithBrowseButton> tscn;
    private LabeledComponent<JTextField> arguments;
    private JCheckBox debugShapes;
    private JCheckBox debugPaths;

    @Override
    public void resetEditorFrom(GdRunConfiguration gdRunConfiguration) {
        godotExe.getComponent().setText(gdRunConfiguration.getGodotExe());
        tscn.getComponent().setText(gdRunConfiguration.getTscn());
        arguments.getComponent().setText(gdRunConfiguration.getArguments());
        debugShapes.setSelected(gdRunConfiguration.getDebugShapes());
        debugPaths.setSelected(gdRunConfiguration.getDebugPaths());
    }

    @Override
    public void applyEditorTo(GdRunConfiguration gdRunConfiguration) {
        gdRunConfiguration.setGodotExe(godotExe.getComponent().getText());
        gdRunConfiguration.setTscn(tscn.getComponent().getText());
        gdRunConfiguration.setArguments(arguments.getComponent().getText());
        gdRunConfiguration.setDebugShapes(debugShapes.isSelected());
        gdRunConfiguration.setDebugPaths(debugPaths.isSelected());
    }

    @Override
    public @NotNull JComponent createEditor() {
        return myPanel;
    }

    private void createUIComponents() {
        godotExe = new LabeledComponent<>();
        tscn = new LabeledComponent<>();
        arguments = new LabeledComponent<>();
        TextFieldWithBrowseButton godotField = new TextFieldWithBrowseButton();
        godotField.setText("");
        godotField.setToolTipText(GdScriptBundle.message("settings.path.to.godot.executable"));
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

        JTextField argumentsField = new JTextField();
        argumentsField.setText(GdScriptBundle.message("settings.options"));
        argumentsField.setToolTipText(GdScriptBundle.message("settings.options"));
        arguments.setComponent(argumentsField);
    }

}
