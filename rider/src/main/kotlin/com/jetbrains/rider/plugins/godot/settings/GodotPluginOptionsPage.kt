package com.jetbrains.rider.plugins.godot.settings

import com.jetbrains.rider.settings.simple.SimpleOptionsPage

class GodotPluginOptionsPage : SimpleOptionsPage("Godot Engine", "GodotPluginSettings") {
    override fun getId(): String {
        return "preferences.build.godotPlugin"
    }

    override fun getHelpTopic(): String {
        return "Settings_Godot_Engine"
    }
}
