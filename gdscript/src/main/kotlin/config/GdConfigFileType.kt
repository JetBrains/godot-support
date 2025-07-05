package config

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object GdConfigFileType : LanguageFileType(GdConfigLanguage) {

    override fun getName(): String = "GodotConfig file"

    override fun getDescription(): String = "GodotConfig file"

    override fun getDefaultExtension(): String = "gdconf"

    override fun getIcon(): Icon = GdScriptPluginIcons.Icons.GodotConfigFile

}
