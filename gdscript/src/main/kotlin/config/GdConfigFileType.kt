package config

import GdScriptPluginIcons
import com.intellij.openapi.fileTypes.LanguageFileType
import gdscript.GdScriptBundle
import javax.swing.Icon

object GdConfigFileType : LanguageFileType(GdConfigLanguage) {

    override fun getName(): String = "GodotConfig file"

    override fun getDescription(): String = GdScriptBundle.message("label.godotconfig.file")

    override fun getDefaultExtension(): String = "gdconf"

    override fun getIcon(): Icon = GdScriptPluginIcons.Icons.GodotConfigFile

}
