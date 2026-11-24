package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.openapi.project.DumbAware
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationFactory
import icons.RiderIcons
import org.jetbrains.annotations.NonNls

class GodotPlayerConfigurationType : ConfigurationTypeBase(
    ID,
    GodotPluginBundle.message("godot.run.player.name"),
    GodotPluginBundle.message("godot.run.player.description"),
    RiderIcons.RunConfigurations.DotNetExecutable,
), DumbAware {
    val factory: DotNetExeConfigurationFactory = DotNetExeConfigurationFactory(this)

    init {
        addFactory(factory)
    }

    companion object {
        @NonNls
        const val ID: String = "GodotPlayer"
    }
}