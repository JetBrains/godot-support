package gdscript.settings

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.JBIntSpinner
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.listCellRenderer.textListCellRenderer
import gdscript.GdScriptBundle
import org.jetbrains.annotations.Nls
import javax.swing.JComponent
import javax.swing.JLabel
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
    private val lspRemoteHostPortSpinner = JBIntSpinner(6005, 1, 65535)
    private val lspDynamicPortHint = JLabel(GdScriptBundle.message("settings.label.lsp.port.dynamic.hint"))

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

        panel = panel {
            group(GdScriptBundle.message("settings.separator.lsp")) {
                row(GdScriptBundle.message("settings.label.lsp.connection.mode")) {
                    cell(lspConnectionModeCb)
                }
                row(GdScriptBundle.message("settings.label.lsp.port")) {
                    cell(lspRemoteHostPortSpinner)
                    cell(lspDynamicPortHint)
                }
            }

            group(GdScriptBundle.message("settings.separator.documentation")) {
                row(GdScriptBundle.message("settings.label.documentation.provider")) {
                    cell(docProviderCb)
                }
            }

            group(GdScriptBundle.message("settings.separator.completion")) {
                row { cell(hidePrivateCheck) }
            }

            group(GdScriptBundle.message("settings.separator.other")) {
                row { cell(shortTypedCheck) }
                row(GdScriptBundle.message("settings.label.reference.node.resource.checks")) {
                    cell(annotatorsCb)
                }

                row(GdScriptBundle.message("settings.label.critical.comments")) {
                    cell(criticalsTf).align(AlignX.FILL)
                }
                row(GdScriptBundle.message("settings.label.warning.comments")) {
                    cell(warningsTf).align(AlignX.FILL)
                }
                row(GdScriptBundle.message("settings.label.note.comments")) {
                    cell(notesTf).align(AlignX.FILL)
                }
                row {
                    cell(JPanel())
                }.resizableRow()
            }
        }
    }

    private fun updateLspControlsState() {
        val mode = lspConnectionMode
        lspRemoteHostPortSpinner.isEnabled = mode == GdLspConnectionMode.ConnectRunningEditor
        lspDynamicPortHint.isVisible = mode == GdLspConnectionMode.StartEditorHeadless
    }

    fun preferredFocusedComponent(): JComponent = lspConnectionModeCb

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
        get() = lspRemoteHostPortSpinner.number
        set(newStatus) {
            lspRemoteHostPortSpinner.value = newStatus
        }

    var docProvider: GdDocProviderMode
        get() = docProviderCb.selectedItem as GdDocProviderMode
        set(newStatus) {
            docProviderCb.selectedItem = newStatus
        }

}
