package tscn

import com.intellij.openapi.fileTypes.LanguageFileType
import gdscript.GdScriptBundle
import javax.swing.Icon

object TscnFileType : LanguageFileType(TscnLanguage) {
    override fun getName(): String = "Tscn file"

    override fun getDescription(): String = GdScriptBundle.message("filetype.tscn.file.description")

    override fun getDefaultExtension(): String = "tscn"

    override fun getIcon(): Icon = GdScriptPluginIcons.TscnIcons.FILE
}
