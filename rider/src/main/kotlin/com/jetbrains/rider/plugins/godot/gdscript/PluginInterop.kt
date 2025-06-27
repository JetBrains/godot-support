package com.jetbrains.rider.plugins.godot.gdscript

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import org.jetbrains.annotations.ApiStatus

val GDSCRIPT_PLUGIN_ID: PluginId
    @ApiStatus.Internal
    get() = PluginId.getId("com.intellij.rider.godot.gdscript")

class PluginInterop {
    companion object{

        fun isGdScriptPluginInstalled() = PluginManagerCore.plugins.any { it.pluginId == GDSCRIPT_PLUGIN_ID }

        fun isGdScriptPluginEnabled() = PluginManagerCore.plugins.any { it.pluginId == GDSCRIPT_PLUGIN_ID && it.isEnabled }
    }
}