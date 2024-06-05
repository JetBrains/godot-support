package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.fileTypes.FileType
import org.jetbrains.plugins.textmate.TextMateBackedFileType
import javax.swing.Icon

internal class GdScriptFileType private constructor() : FileType, TextMateBackedFileType {
  override fun getName(): String {
    return "GDScript"
  }

  override fun getDescription(): String {
    return GodotPluginBundle.message("file.type.description")
  }

  override fun getDisplayName(): String {
    return GodotPluginBundle.message("file.type.display.name")
  }

  override fun getDefaultExtension(): String {
    return "gd"
  }

  override fun getIcon(): Icon {
    return GodotIcons.Icons.GodotLogo
  }

  override fun isBinary(): Boolean {
    return false
  }
}