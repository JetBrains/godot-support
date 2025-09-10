package com.jetbrains.rider.plugins.godot.run.configurations

import GodotDebugRunFactory
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.openapi.project.DumbAware
import com.jetbrains.rider.plugins.godot.GodotIcons
import com.jetbrains.rider.plugins.godot.GodotPluginBundle

class GodotDebugRunConfigurationType : ConfigurationTypeBase(id,
                                                             GodotPluginBundle.message("godot.3.start.and.debug"),
                                                             GodotPluginBundle.message("start.godot.and.attach.debugger"),
                                                             GodotIcons.RunConfigurations.StartAndDebug), DumbAware {
    val factory = GodotDebugRunFactory(this)

    init {
        addFactory(factory)
    }

    companion object {
        const val id = "GODOT_DEBUG_RUN_CONFIGURATION"
    }
}
