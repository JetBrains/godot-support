package gdscript.formatter.settings

import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import gdscript.GdLanguage
import gdscript.formatter.GdCodeStyleSettings
import gdscript.formatter.settings.GdSettings.consume

class GdLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    override fun getLanguage(): Language {
        return GdLanguage;
    }

    override fun getIndentOptionsEditor(): IndentOptionsEditor {
        return SmartIndentOptionsEditor();
    }

    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings =
        GdCodeStyleSettings(settings);

    override fun customizeSettings(
        consumer: CodeStyleSettingsCustomizable,
        settingsType: SettingsType,
    ) {
        when (settingsType) {
            SettingsType.SPACING_SETTINGS -> {
                GdSettings.SPACING.consume(consumer);
            }

            SettingsType.INDENT_SETTINGS -> {
                consumer.showAllStandardOptions()
            }

            SettingsType.BLANK_LINES_SETTINGS -> {
                GdSettings.BLANK_LINES.consume(consumer);
            }

            SettingsType.WRAPPING_AND_BRACES_SETTINGS -> {
                /* TODO */
            }

            SettingsType.COMMENTER_SETTINGS -> {
                /* TODO */
            }

            SettingsType.LANGUAGE_SPECIFIC -> {
                /* TODO */
            }
        }
    }

    override fun getCodeSample(settingsType: SettingsType): String {
        return """extends Node2D
            |class_name MyClass
            |
            |var vari: int = 1;
            |var var2: String = "";
            |
            |const cc = "cc";
            |
            |func fn1():
            |   pass;
            |
            |
            |func fn2():
            |   pass;
        """.trimMargin()
    }

}
