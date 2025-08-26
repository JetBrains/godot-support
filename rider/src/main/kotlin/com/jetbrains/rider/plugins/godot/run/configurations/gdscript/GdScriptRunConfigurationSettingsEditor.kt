package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.intellij.execution.impl.CheckableRunConfigurationEditor
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.LifetimeDefinition
import com.jetbrains.rider.plugins.godot.GodotProjectLifetimeService
import javax.swing.JPanel

class GdScriptRunConfigurationSettingsEditor(project: Project) : SettingsEditor<GdScriptRunConfiguration>(),
                                                                 CheckableRunConfigurationEditor<GdScriptRunConfiguration> {

    private val lifetimeDefinition: LifetimeDefinition = GodotProjectLifetimeService.getNestedLifetimeDefinition(project)
    private val form: GdScriptEditorForm = GdScriptEditorForm(lifetimeDefinition, project)

    override fun checkEditorData(configuration: GdScriptRunConfiguration) {
        form.update(configuration)
    }

    override fun resetEditorFrom(configuration: GdScriptRunConfiguration) {
        form.setData(configuration.structured)
    }

    override fun applyEditorTo(configuration: GdScriptRunConfiguration) {
        checkEditorData(configuration)
    }

    override fun createEditor(): JPanel {
        return form.panel
    }

    override fun disposeEditor() {
        lifetimeDefinition.terminate()
    }
}