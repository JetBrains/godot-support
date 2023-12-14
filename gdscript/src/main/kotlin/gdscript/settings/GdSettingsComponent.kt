package gdscript.settings

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.impl.libraries.LibraryEx
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBCheckBox
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.util.ui.FormBuilder
import gdscript.library.GdLibraryManager
import gdscript.library.GdLibraryProperties
import gdscript.settings.component.GdSettingsComponents
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

class GdSettingsComponent(val project: Project) {
    val panel: JPanel

    private val hidePrivateCheck = JBCheckBox("Hide _private members from completion")
    private val shortTypedCheck = JBCheckBox("Use short typing 'var a := 1' instead of 'var a: int = 1'")
    private val annotatorsCb = ComboBox<String>()
    private val selectSdk = GdSettingsComponents.selectSdk()
    private val addSdk: JButton = GdSettingsComponents.addSdk(selectSdk, this)
    private val removeAllSdks: JButton = JButton("Remove all SDKs")
    private val criticalsTf: JTextField = JTextField()
    private val warningsTf: JTextField = JTextField()
    private val notesTf: JTextField = JTextField()

    init {
        annotatorsCb.addItem(GdProjectState.OFF)
        annotatorsCb.addItem(GdProjectState.WARN)
        annotatorsCb.addItem(GdProjectState.ERR)
        annotatorsCb.selectedItem = GdProjectState.OFF

        val layout = GridLayoutManager(1, 2)
        val sdkPanel = JPanel(layout)
        val dimension = Dimension(-1, -1)
        sdkPanel.add(selectSdk, GridConstraints(0, 0, 1, 1, 8, 1, 2, 0, dimension, dimension, dimension, 0, false))
        sdkPanel.add(addSdk, GridConstraints(0, 1, 1, 1, 8, 0, 0, 0, dimension, dimension, Dimension(30, 30), 0, false))

        removeAllSdks.addActionListener {
            sdkPath = null
            selectSdk.removeAllItems()
            GdLibraryManager.clearSdks()
        }

        panel = FormBuilder.createFormBuilder()
                .addLabeledComponent("GdScript SDK", sdkPanel, 1, true)
                .addComponent(removeAllSdks, 1)
                .addComponent(hidePrivateCheck, 1)
                .addComponent(shortTypedCheck, 1)
                .addLabeledComponent("Reference, Node, Resource checks", annotatorsCb, 1)
                .addLabeledComponent("Critical comments", criticalsTf, 1)
                .addLabeledComponent("Warning comments", warningsTf, 1)
                .addLabeledComponent("Note comments", notesTf, 1)
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
        get() {
            try {
                return ((selectSdk.selectedItem as LibraryEx?)?.properties?.state as GdLibraryProperties?)?.path
            } catch (e: Exception) {
                return null
            }
        }
        set(path) {
            for (i in 0 until selectSdk.itemCount) {
                if (((selectSdk.getItemAt(i) as LibraryEx).properties.state as GdLibraryProperties).path == path) {
                    selectSdk.selectedIndex = i
                    break
                }
            }
        }

    var shortTyped: Boolean
        get() = shortTypedCheck.isSelected
        set(newStatus) {
            shortTypedCheck.isSelected = newStatus
        }

    var annotators: String
        get() = annotatorsCb.selectedItem as String
        set(newStatus) {
            annotatorsCb.selectedItem = newStatus
        }

    var criticals: String
        get() = criticalsTf.text
        set(newStatus) {
            criticalsTf.text = newStatus
        }

    var warnings: String
        get() = warningsTf.text
        set(newStatus) {
            warningsTf.text = newStatus
        }

    var notes: String
        get() = notesTf.text
        set(newStatus) {
            notesTf.text = newStatus
        }

}
