package gdscript.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import gdscript.library.GdLibraryManager;
import javax.swing.*;
import java.awt.event.*;

public class GdDownloadSdk extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> versionCb;
    public TextFieldWithBrowseButton locationFc;
    private JLabel VersionLb;
    private JLabel LocationLb;

    public String path = "";
    public String version = "";

    public GdDownloadSdk() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        path = locationFc.getText();
        version = (String) versionCb.getSelectedItem();
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void createUIComponents() {
        versionCb = new ComboBox<>();
        locationFc = new TextFieldWithBrowseButton();
        locationFc.addBrowseFolderListener(new TextBrowseFolderListener(
                FileChooserDescriptorFactory.createSingleFolderDescriptor()
        ));

        GdLibraryManager.INSTANCE.listRemoteSdks(versionCb);
    }

    public static void main(String[] args) {
        GdDownloadSdk dialog = new GdDownloadSdk();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

}
