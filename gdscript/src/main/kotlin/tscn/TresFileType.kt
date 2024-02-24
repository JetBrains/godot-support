package tscn

import com.intellij.openapi.fileTypes.LanguageFileType
import org.jetbrains.plugins.textmate.TextMateBackedFileType
import javax.swing.Icon

object TresFileType : LanguageFileType(TscnLanguage), TextMateBackedFileType {

    override fun getName(): String = "Tres file"

    override fun getDescription(): String = "Godot's Text resource file"

    override fun getDefaultExtension(): String = "tres"

    override fun getIcon(): Icon = GodotFileIcon.FILE
}
