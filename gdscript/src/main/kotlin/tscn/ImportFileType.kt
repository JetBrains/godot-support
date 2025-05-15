package tscn

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object ImportFileType : LanguageFileType(TscnLanguage) {

    override fun getName(): String = "Import file"

    override fun getDescription(): String = "Godot's resource tracker import file"

    override fun getDefaultExtension(): String = "import"

    override fun getIcon(): Icon = GdScriptPluginIcons.Icons.GodotFile

    override fun getDisplayName(): String = "Godot import file"
}
