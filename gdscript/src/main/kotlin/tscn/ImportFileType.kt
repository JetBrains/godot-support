package tscn

import com.intellij.openapi.fileTypes.LanguageFileType
import gdscript.GdScriptBundle
import javax.swing.Icon

object ImportFileType : LanguageFileType(TscnLanguage) {

    override fun getName(): String = "Import file"

    override fun getDescription(): String = GdScriptBundle.message("filetype.godot.import.description")

    override fun getDefaultExtension(): String = "import"

    override fun getIcon(): Icon = GdScriptPluginIcons.Icons.GodotFile

    override fun getDisplayName(): String = GdScriptBundle.message("filetype.godot.import.display.name")
}
