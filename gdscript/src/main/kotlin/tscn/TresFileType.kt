package tscn

import GdScriptPluginIcons
import com.intellij.openapi.fileTypes.LanguageFileType
import gdscript.GdScriptBundle
import javax.swing.Icon

object TresFileType : LanguageFileType(TscnLanguage) {

    override fun getName(): String = "Tres file"

    override fun getDescription(): String = GdScriptBundle.message("filetype.tscn.tres.file.description")

    override fun getDefaultExtension(): String = "tres"

    override fun getIcon(): Icon = GdScriptPluginIcons.Icons.GodotFile

    override fun getDisplayName(): String = GdScriptBundle.message("filetype.tscn.tres.file.display.name")
}
