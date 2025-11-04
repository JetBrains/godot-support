package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.intellij.json.JsonLanguage
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.createNestedDisposable
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.ui.ex.MultiLineLabel
import com.intellij.ui.LanguageTextField
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.panel
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.threading.coroutines.async
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.actions.StartGodotEditorAction
import kotlinx.coroutines.Dispatchers
import javax.swing.JPanel
import javax.swing.event.HyperlinkEvent

class GdScriptEditorForm(lifetime: Lifetime, val project: Project) {

    private lateinit var fullArgumentsField: LanguageTextField
    private lateinit var errorLabel: MultiLineLabel
    private lateinit var errorRow: Row

    val panel: JPanel = panel {
        indent {
            row {
                text(GodotPluginBundle.message("editor.should.be.running"), ) {
                    if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                        lifetime.async(Dispatchers.Default) {
                            StartGodotEditorAction.startEditor(project)
                        }
                    }
                }
            }

            row {
                fullArgumentsField = LanguageTextField(JsonLanguage.INSTANCE, project, "", false)
                cell(fullArgumentsField)
                    .align(AlignX.FILL)
                    .resizableColumn()
                    .comment(GodotPluginBundle.message("gdscript.editor.form.arguments.comment"))

                // custom validation, since I wasn't able to setup the Platform one with LanguageTextField
                fullArgumentsField.document.addDocumentListener(object : DocumentListener {
                    override fun documentChanged(event: DocumentEvent) {
                        super.documentChanged(event)
                        val res = validateJson(event.document.text)
                        if (res != null) {
                            errorLabel.text = res.message
                            errorRow.visible(true)
                        }
                        else {
                            errorLabel.text = ""
                            errorRow.visible(false)
                        }
                    }
                }, lifetime.createNestedDisposable())
            }
            errorRow = row {
                errorLabel = MultiLineLabel("")
                cell(errorLabel)
                    .resizableColumn()
            }
            errorRow.visible(false)
        }
    }.apply {name = "GdScriptEditorForm"}

    private fun validateJson(text: String): ValidationInfo? {
        if (text.isBlank()) return null // allow empty, will use defaults
        val mapper = GdScriptRunConfigJacksonObjectMapper.getService(project).mapper
        val node = try {
            mapper.readTree(text)
        } catch (e: Exception) {
            return ValidationInfo(GodotPluginBundle.message("gdscript.editor.form.arguments.error.invalid.json"), fullArgumentsField)
        }
        if (!node.isObject) {
            return ValidationInfo(GodotPluginBundle.message("gdscript.editor.form.arguments.error.root.object"), fullArgumentsField)
        }
        // request
        val requestNode = node.get("request")
        if (requestNode != null && requestNode.isTextual) {
            val value = requestNode.asText()
            val valid = try {
                kotlin.runCatching { com.intellij.platform.dap.DapStartRequest.valueOf(value) }.isSuccess
            } catch (_: Exception) { false }
            if (!valid) {
                return ValidationInfo(GodotPluginBundle.message("gdscript.editor.form.arguments.error.request"), fullArgumentsField)
            }
        } else if (requestNode != null && !requestNode.isTextual) {
            return ValidationInfo(GodotPluginBundle.message("gdscript.editor.form.arguments.error.request"), fullArgumentsField)
        }
        // debugServer can be integer or string that parses to int
        val portNode = node.get("debugServer")
        if (portNode != null) {
            val ok = when {
                portNode.isInt -> true
                portNode.isIntegralNumber -> true
                portNode.isTextual -> portNode.asText().toIntOrNull() != null
                else -> false
            }
            if (!ok) {
                return ValidationInfo(GodotPluginBundle.message("gdscript.editor.form.arguments.error.debugServer"), fullArgumentsField)
            }
        }
        // scene must be string if present
        val sceneNode = node.get("scene")
        if (sceneNode != null && !sceneNode.isTextual) {
            return ValidationInfo(GodotPluginBundle.message("gdscript.editor.form.arguments.error.scene"), fullArgumentsField)
        }
        return null
    }

    fun setData(json: String) {
        fullArgumentsField.text = json
    }

    fun getData(viewModel: GdScriptRunConfiguration) {
        viewModel.json = fullArgumentsField.text
    }
}