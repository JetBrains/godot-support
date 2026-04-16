package gdscript.settings

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.TitledSeparator
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.listCellRenderer.textListCellRenderer
import com.intellij.util.ui.FormBuilder
import gdscript.GdScriptBundle
import org.jetbrains.annotations.Nls
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JTextField

class GdSettingsComponent(val project: Project) {
    val panel: JPanel

    private val hidePrivateCheck = JBCheckBox(GdScriptBundle.message("settings.checkbox.hide.private.members.from.completion"))
    private val shortTypedCheck = JBCheckBox(GdScriptBundle.message("settings.checkbox.use.short.typing.var.instead.var.int"))
    private val annotatorsCb = ComboBox<String>()
    private val docProviderCb = ComboBox<GdDocProviderMode>()
    private val criticalsTf: JTextField = JTextField()
    private val warningsTf: JTextField = JTextField()
    private val notesTf: JTextField = JTextField()

    private val lspConnectionModeCb = ComboBox<GdLspConnectionMode>()
    private val lspUseDynamicPortCheck = JBCheckBox(GdScriptBundle.message("settings.checkbox.lsp.use.dynamic.port"))
    private val lspRemoteHostPortTf: JTextField = JTextField()

    init {
        annotatorsCb.addItem(GdProjectState.OFF)
        annotatorsCb.addItem(GdProjectState.WARN)
        annotatorsCb.addItem(GdProjectState.ERR)

        docProviderCb.addItem(GdDocProviderMode.GDSCRIPT)
        docProviderCb.addItem(GdDocProviderMode.LSP)
        docProviderCb.renderer = object : SimpleListCellRenderer<GdDocProviderMode>() {
            override fun customize(list: JList<out GdDocProviderMode>, value: GdDocProviderMode?, index: Int, selected: Boolean, hasFocus: Boolean) {
                text = when (value) {
                    GdDocProviderMode.LSP -> GdScriptBundle.message("settings.doc.provider.lsp")
                    else -> GdScriptBundle.message("settings.doc.provider.gdscript")
                }
            }
        }

        for (mode in GdLspConnectionMode.entries) {
            lspConnectionModeCb.addItem(mode)
        }
        lspConnectionModeCb.setRenderer(textListCellRenderer("") {
            when (it) {
                GdLspConnectionMode.Never -> GdScriptBundle.message("label.never.use.lsp")
                GdLspConnectionMode.ConnectRunningEditor -> GdScriptBundle.message("label.attempt.to.connect.to.running.godot.editor")
                GdLspConnectionMode.StartEditorHeadless -> GdScriptBundle.message("label.automatically.start.headless.lsp.server")
            }
        })

        lspConnectionModeCb.addActionListener { updateLspControlsState() }
        lspUseDynamicPortCheck.addActionListener { updateLspControlsState() }

        panel = FormBuilder.createFormBuilder()
            .addComponent(hidePrivateCheck, 1)
            .addComponent(shortTypedCheck, 1)
            .addLabeledComponent(GdScriptBundle.message("settings.label.reference.node.resource.checks"), annotatorsCb, 1)
            // TODO: Consider grouping together with other "Other" settings
            .addLabeledComponent(GdScriptBundle.message("settings.label.documentation.provider"), docProviderCb, 1)
            .addLabeledComponent(GdScriptBundle.message("settings.label.critical.comments"), criticalsTf, 1)
            .addLabeledComponent(GdScriptBundle.message("settings.label.warning.comments"), warningsTf, 1)
            .addLabeledComponent(GdScriptBundle.message("settings.label.note.comments"), notesTf, 1)
            .addComponent(TitledSeparator(GdScriptBundle.message("settings.separator.lsp")), 1)
            .addLabeledComponent(GdScriptBundle.message("settings.label.lsp.connection.mode"), lspConnectionModeCb, 1)
            .addComponent(lspUseDynamicPortCheck, 1)
            .addLabeledComponent(GdScriptBundle.message("settings.label.lsp.port"), lspRemoteHostPortTf, 1)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    private fun updateLspControlsState() {
        val connecting = lspConnectionMode != GdLspConnectionMode.Never
        lspUseDynamicPortCheck.isEnabled = connecting
        lspRemoteHostPortTf.isEnabled = connecting && !lspUseDynamicPortCheck.isSelected
    }

    fun preferredFocusedComponent(): JComponent = hidePrivateCheck

    var hidePrivate: Boolean
        get() = hidePrivateCheck.isSelected
        set(newStatus) {
            hidePrivateCheck.isSelected = newStatus
        }

    var shortTyped: Boolean
        get() = shortTypedCheck.isSelected
        set(newStatus) {
            shortTypedCheck.isSelected = newStatus
        }

    var annotators: String
        get() = annotatorsCb.selectedItem as String
        set(@Nls newStatus) {
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

    var lspConnectionMode: GdLspConnectionMode
        get() = lspConnectionModeCb.selectedItem as GdLspConnectionMode
        set(newStatus) {
            lspConnectionModeCb.selectedItem = newStatus
            updateLspControlsState()
        }

    var lspRemoteHostPort: Int
        get() = lspRemoteHostPortTf.text.toIntOrNull() ?: 6005
        set(newStatus) {
            lspRemoteHostPortTf.text = newStatus.toString()
        }

    var lspUseDynamicPort: Boolean
        get() = lspUseDynamicPortCheck.isSelected
        set(newStatus) {
            lspUseDynamicPortCheck.isSelected = newStatus
            updateLspControlsState()
        }

    var docProvider: GdDocProviderMode
        get() = docProviderCb.selectedItem as GdDocProviderMode
        set(newStatus) {
            docProviderCb.selectedItem = newStatus
        }

}
