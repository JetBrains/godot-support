package com.jetbrains.rider.godot.community.tscn

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.rider.plugins.godot.community.icons.RiderPluginsGodotCommunityIcons
import com.jetbrains.rider.godot.community.GodotCommunityBundle
import javax.swing.Icon

object TscnFileType : LanguageFileType(TscnLanguage) {
    override fun getName(): String = "Tscn file"

    override fun getDescription(): String = GodotCommunityBundle.message("filetype.tscn.file.description")

    override fun getDefaultExtension(): String = "tscn"

    override fun getIcon(): Icon = RiderPluginsGodotCommunityIcons.TscnFile
}
