package com.jetbrains.rider.plugins.godot.run.configurations

import GodotDebugFactory
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.jetbrains.rider.plugins.godot.GodotIcons

class GodotDebugConfigurationType : ConfigurationTypeBase(id,
        "Start and Debug", "Start Godot and Attach debugger",
        GodotIcons.RunConfigurations.StartAndDebug) {

    val debugFactory = GodotDebugFactory(this)

    init {
        addFactory(debugFactory)
    }

    companion object {
        val id = "GODOT_DEBUG_RUN_CONFIGURATION"
    }
}
