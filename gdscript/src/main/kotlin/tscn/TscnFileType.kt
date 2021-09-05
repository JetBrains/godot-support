package tscn

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class TscnFileType : LanguageFileType {

    companion object {
        val INSTANCE = TscnFileType();
    }

    constructor(): super(TscnLanguage.INSTANCE)

    override fun getName(): String = "Tscn file";

    override fun getDescription(): String = "Godot's scene file";

    override fun getDefaultExtension(): String = "tscn";

    override fun getIcon(): Icon = TscnIcon.FILE;

}
