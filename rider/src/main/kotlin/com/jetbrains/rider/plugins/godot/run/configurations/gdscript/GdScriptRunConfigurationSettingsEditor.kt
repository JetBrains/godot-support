package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.intellij.execution.impl.CheckableRunConfigurationEditor
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.LifetimeDefinition
import com.jetbrains.rider.plugins.godot.GodotProjectLifetimeService

class GdScriptRunConfigurationSettingsEditor(project: Project) : SettingsEditor<GdScriptRunConfiguration>(),
                                                                 CheckableRunConfigurationEditor<GdScriptRunConfiguration> {

    private val lifetimeDefinition: LifetimeDefinition = GodotProjectLifetimeService.getNestedLifetimeDefinition(project)
    private val viewModel: GdScriptViewModel = GdScriptViewModel(lifetimeDefinition, project)
    private val form: GdScriptEditorForm = GdScriptEditorForm(viewModel)

    init {
        viewModel.port.advise(lifetimeDefinition.lifetime) { fireEditorStateChanged() }
    }

    override fun checkEditorData(configuration: GdScriptRunConfiguration) {
        configuration.port = viewModel.port.value
    }

    override fun resetEditorFrom(configuration: GdScriptRunConfiguration) {
        viewModel.port.value = configuration.port
    }

    override fun applyEditorTo(configuration: GdScriptRunConfiguration) {
        checkEditorData(configuration)
    }

    override fun createEditor() = form.panel

    override fun disposeEditor() {
        lifetimeDefinition.terminate()
    }
}