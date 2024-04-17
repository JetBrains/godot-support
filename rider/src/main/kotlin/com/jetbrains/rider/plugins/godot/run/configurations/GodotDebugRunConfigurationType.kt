package com.jetbrains.rider.plugins.godot.run.configurations

import GodotDebugRunFactory
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.jetbrains.rider.plugins.godot.GodotIcons

class GodotDebugRunConfigurationType : ConfigurationTypeBase(id,
        "Godot 3 Start and Debug", "Start Godot and Attach debugger",
        GodotIcons.RunConfigurations.StartAndDebug) {

    val factory = GodotDebugRunFactory(this)

    init {
        addFactory(factory)
    }

    companion object {
        const val id = "GODOT_DEBUG_RUN_CONFIGURATION"
    }
}
