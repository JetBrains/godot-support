package com.jetbrains.rider.plugins.godot.settings.templates

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.util.NlsSafe
import com.jetbrains.rider.settings.simple.SimpleOptionsPage

class GodotLiveTemplatesOptionPage : SimpleOptionsPage(pageName, "RiderGodotLiveTemplatesSettings"), Configurable.NoScroll {
    companion object {
        @NlsSafe
        const val pageName = "Godot C#"
    }

    override fun getId(): String {
        return pageId
    }

    override fun getHelpTopic(): String {
        return "Reference__Templates_Explorer__Live_Templates_Godot"
    }
}