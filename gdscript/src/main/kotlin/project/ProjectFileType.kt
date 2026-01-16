package project

import com.intellij.openapi.fileTypes.LanguageFileType
import gdscript.GdScriptBundle
import javax.swing.Icon

object ProjectFileType : LanguageFileType(ProjectLanguage) {

    override fun getName(): String = "GodotProject file"

    override fun getDescription(): String = GdScriptBundle.message("project.type.godot.project.description")

    override fun getDefaultExtension(): String = "godot"

    override fun getIcon(): Icon = GdScriptPluginIcons.Icons.GodotProjectFile

}
