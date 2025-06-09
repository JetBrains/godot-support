package com.jetbrains.rider.godot.community.gdscript

import com.intellij.lang.Language
import com.jetbrains.rider.godot.community.GodotCommunityBundle

object GdLanguage : Language("GdScript") {
    override fun getDisplayName(): String = GodotCommunityBundle.message("language.name")
}
