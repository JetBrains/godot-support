package tscn

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object TresFileType : LanguageFileType(TscnLanguage) {

    override fun getName(): String = "Tres file"

    override fun getDescription(): String = "Godot's Text resource file"

    override fun getDefaultExtension(): String = "tres"

    override fun getIcon(): Icon = GodotFileIcon.FILE

    override fun getDisplayName(): String = "tres"
}
