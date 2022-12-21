package project

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object ProjectFileType : LanguageFileType(ProjectLanguage) {

    override fun getName(): String = "GodotProject file";

    override fun getDescription(): String = "Godot's project file";

    override fun getDefaultExtension(): String = "godot";

    override fun getIcon(): Icon = ProjectIcon.FILE;

}
