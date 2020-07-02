package com.jetbrains.rider.plugins.godot.ideaInterop.fileTypes.tscn

import com.intellij.icons.AllIcons
import com.intellij.openapi.fileTypes.LanguageFileType

object TscnFileType : LanguageFileType(TscnLanguage) {
    override fun getIcon() = AllIcons.FileTypes.Text
    override fun getName() = "TSCN"
    override fun getDefaultExtension() = "tscn"
    override fun getDescription() = "Text Scene File (Godot)"
}
