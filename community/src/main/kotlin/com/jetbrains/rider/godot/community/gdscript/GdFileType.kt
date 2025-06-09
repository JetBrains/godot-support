package com.jetbrains.rider.godot.community.gdscript

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.rider.plugins.godot.community.icons.RiderPluginsGodotCommunityIcons
import com.jetbrains.rider.godot.community.GodotCommunityBundle
import javax.swing.Icon

object GdFileType : LanguageFileType(GdLanguage) {
    override fun getName(): String = GdLanguage.id
    override fun getDescription(): String = GodotCommunityBundle.message("language.file_name")
    override fun getDefaultExtension(): String = "gd"
    override fun getIcon(): Icon = RiderPluginsGodotCommunityIcons.GDScript
}
