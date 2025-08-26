package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.openapi.project.PossiblyDumbAware
import com.jetbrains.rider.plugins.godot.GodotIcons
import com.jetbrains.rider.plugins.godot.GodotPluginBundle

class GdScriptConfigurationType : ConfigurationTypeBase(id,
                                                        GodotPluginBundle.message("gdscript.start.and.debug"),
                                                        GodotPluginBundle.message("start.godot.and.attach.debugger"),
                                                        GodotIcons.RunConfigurations.StartAndDebug), PossiblyDumbAware {

    val factory: GdScriptRunFactory = GdScriptRunFactory(this)

    init {
        addFactory(factory)
    }

    // todo: add link https://jb.gg/godot-dap to documentation page
    override fun getHelpTopic(): String = "reference.dialogs.rundebug.gdscript"

    companion object {
        const val id: String = "GDSCRIPT_DEBUG_RUN_CONFIGURATION"
    }
}
