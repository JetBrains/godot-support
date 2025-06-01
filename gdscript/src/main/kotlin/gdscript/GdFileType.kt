package gdscript

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object GdFileType : LanguageFileType(GdLanguage) {
    override fun getName(): String = GdLanguage.id
    override fun getDescription(): String = GdScriptBundle.message("language.file_name")
    override fun getDefaultExtension(): String = "gd"
    override fun getIcon(): Icon = GdScriptPluginIcons.Icons.GDScript
}
