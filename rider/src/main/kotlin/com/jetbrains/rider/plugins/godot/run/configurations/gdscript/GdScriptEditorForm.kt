package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.intellij.icons.AllIcons
import com.intellij.json.JsonLanguage
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.createNestedDisposable
import com.intellij.platform.dap.DapStartRequest
import com.intellij.ui.LanguageTextField
import com.intellij.ui.PortField
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.panel
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.threading.coroutines.async
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.actions.StartGodotEditorAction
import kotlinx.coroutines.Dispatchers
import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox
import javax.swing.JPanel
import javax.swing.event.HyperlinkEvent

class GdScriptEditorForm(lifetime: Lifetime, project: Project) {

    private lateinit var portField: PortField
    private lateinit var requestCombo: JComboBox<DapStartRequest>
    private lateinit var fullArgumentsField: LanguageTextField
    private lateinit var remainingArgumentsField: LanguageTextField
    private lateinit var sceneField: JBTextField
    private enum class EditMode { Structured, Raw }
    private var editMode: EditMode = EditMode.Structured

    // Row references for visibility toggling
    private var rawArgsRowRef: Row? = null
    private var structuredRowRef: Row? = null

    fun setVisibility(structured: Boolean) {
        rawArgsRowRef?.visible(!structured)
        structuredRowRef?.visible(structured)
    }

    fun setMode(structured: Boolean) {
        // sync between structured and raw views
        if (structured){
            val structuredVal = GdScriptRunConfigurationHelper.parse(fullArgumentsField.text)
            portField.value = structuredVal.debugServerPort
            requestCombo.selectedItem = structuredVal.request
            sceneField.text = structuredVal.scene
            remainingArgumentsField.text = structuredVal.remainingArguments
            // ensure enabled state reflects current request
            updateStructuredControlsEnabled()
        }
        else{
            val structuredVal = GdScriptStructuredArguments(requestCombo.selectedItem as DapStartRequest, portField.value as Int, remainingArgumentsField.text, sceneField.text )
            fullArgumentsField.text = GdScriptRunConfigurationHelper.serialize(structuredVal)
        }
    }

    val panel: JPanel = panel {
        indent {
            row{
                text(GodotPluginBundle.message("editor.should.be.running")){
                    if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                        lifetime.async(Dispatchers.Default) {
                            StartGodotEditorAction.startEditor(project)
                        }
                    }
                }
            }

            group("", indent = false) {
                row {
                    val items = listOf(EditMode.Structured, EditMode.Raw)
                    val segmented = segmentedButton(items) { mode ->
                        text = when (mode) {
                            EditMode.Structured -> GodotPluginBundle.message("gdscript.editor.form.mode.structured")
                            EditMode.Raw -> GodotPluginBundle.message("gdscript.editor.form.mode.raw")
                        }
                        toolTipText = when (mode) {
                            EditMode.Structured -> GodotPluginBundle.message("gdscript.editor.form.mode.structured.tooltip")
                            EditMode.Raw -> GodotPluginBundle.message("gdscript.editor.form.mode.raw.tooltip")
                        }
                        icon = when (mode) {
                            EditMode.Structured -> AllIcons.Actions.Edit
                            EditMode.Raw -> AllIcons.Json.Object
                        }
                    }
                    segmented.whenItemSelectedFromUi(lifetime.createNestedDisposable()) { selected ->
                        editMode = selected
                        setVisibility(selected == EditMode.Structured)
                        setMode(selected == EditMode.Structured)
                    }
                    segmented.selectedItem = editMode
                }

                rawArgsRowRef = row {
                    fullArgumentsField = LanguageTextField(JsonLanguage.INSTANCE, project, "", false)
                    cell(fullArgumentsField).resizableColumn().align(AlignX.FILL)
                        .comment(GodotPluginBundle.message("gdscript.editor.form.arguments.tooltip"))
                }

                structuredRowRef = row {
                    panel{
                        // Structured mode: port + request + remaining arguments
                        row(GodotPluginBundle.message("gdscript.editor.form.port")) {
                            portField = PortField()
                            cell(portField)
                        }

                        row(GodotPluginBundle.message("gdscript.editor.form.request")) {
                            requestCombo = JComboBox(DefaultComboBoxModel(DapStartRequest.entries.toTypedArray()))
                            cell(requestCombo)
                        }

                        row(GodotPluginBundle.message("gdscript.editor.form.scene")) {
                            sceneField = JBTextField()

                            cell(sceneField).resizableColumn().align(AlignX.FILL).comment(GodotPluginBundle.message("label.note.scene.debugging.requires.godot.see.pr"))
                        }

                        row(GodotPluginBundle.message("gdscript.editor.form.arguments")) {
                            remainingArgumentsField = LanguageTextField(JsonLanguage.INSTANCE, project, "", false)
                            remainingArgumentsField.toolTipText = GodotPluginBundle.message("gdscript.editor.form.arguments.tooltip")
                            cell(remainingArgumentsField).resizableColumn().align(AlignX.FILL)
                                .comment(GodotPluginBundle.message("gdscript.editor.form.arguments.tooltip"))
                        }
                    }
                }
            }
        }
    }


    init {
        // Ensure this panel is identifiable in UI Inspector
        panel.name = "GdScriptEditorForm"
        // Wire up request change to enable/disable other controls in structured mode
        requestCombo.addActionListener { updateStructuredControlsEnabled() }
        // Default to Structured (controls) view
        setVisibility(true)
    }

    fun setData(structured: GdScriptStructuredArguments) {
        requestCombo.selectedItem = structured.request
        portField.value = structured.debugServerPort
        remainingArgumentsField.text = structured.remainingArguments
        sceneField.text = structured.scene
        updateStructuredControlsEnabled()
    }

    private fun updateStructuredControlsEnabled() {
        val isAttach = (requestCombo.selectedItem as? DapStartRequest) == DapStartRequest.Attach
        val enabled = !isAttach
        sceneField.isEnabled = enabled
        remainingArgumentsField.isEnabled = enabled
    }

    fun update(viewModel: GdScriptRunConfiguration) {
        viewModel.structured.scene = sceneField.text
        viewModel.structured.request = requestCombo.selectedItem as DapStartRequest
        viewModel.structured.debugServerPort = portField.value as Int
        viewModel.structured.remainingArguments = remainingArgumentsField.text
    }
}