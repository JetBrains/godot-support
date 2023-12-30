package tscn

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object TscnFileType : LanguageFileType(TscnLanguage) {

    override fun getName(): String = "Tscn file"

    override fun getDescription(): String = "Godot's scene file"

    override fun getDefaultExtension(): String = "tscn"

    override fun getIcon(): Icon = TscnIcon.FILE

}
