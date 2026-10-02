package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationSingletonPolicy
import com.intellij.execution.configurations.VirtualConfigurationType
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import icons.RiderIcons

class GodotInEditorDebugRunFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun getId(): String = "Debug in Godot Editor"

    override fun getSingletonPolicy(): RunConfigurationSingletonPolicy = RunConfigurationSingletonPolicy.SINGLE_INSTANCE_ONLY

    override fun createTemplateConfiguration(project: Project): RunConfiguration =
        GodotInEditorDebugRunConfiguration(project, this)
}

class GodotInEditorDebugRunConfigurationType : ConfigurationTypeBase(
    ID,
    GodotPluginBundle.message("godot.debug.in.editor.configuration.type.name"),
    GodotPluginBundle.message("godot.debug.in.editor.configuration.type.description"),
    RiderIcons.RunConfigurations.DotNetExecutable,
), VirtualConfigurationType, DumbAware {
    val factory: GodotInEditorDebugRunFactory = GodotInEditorDebugRunFactory(this)

    init {
        addFactory(factory)
    }

    companion object {
        const val ID: String = "GODOT_IN_EDITOR_DEBUG_RUN_CONFIGURATION"
    }
}
