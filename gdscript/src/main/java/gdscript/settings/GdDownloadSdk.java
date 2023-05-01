package gdscript.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
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
    private TextFieldWithBrowseButton locationFc;
    private JTextField locationFcInner;
    private JLabel VersionLb;
    private JLabel LocationLb;

    public GdDownloadSdk() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

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
        // TODO add your code here
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void createUIComponents() {
        locationFcInner = new JTextField();
        versionCb = new ComboBox<>();
        locationFc = new TextFieldWithBrowseButton();
        locationFc.addBrowseFolderListener(new TextBrowseFolderListener(new FileChooserDescriptor(
                false,
                true,
                false,
                false,
                false,
                false)));

        GdLibraryManager.INSTANCE.listRemoteSdks(versionCb);
    }

}
