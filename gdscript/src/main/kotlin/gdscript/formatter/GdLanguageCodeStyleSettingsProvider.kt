package gdscript.formatter

import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.*
import gdscript.GdLanguage

class GdLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    override fun getLanguage(): Language {
        return GdLanguage;
    }

    override fun getIndentOptionsEditor(): IndentOptionsEditor {
        return SmartIndentOptionsEditor();
    }

    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings = GdCodeStyleSettings(settings);

    override fun customizeSettings(
        consumer: CodeStyleSettingsCustomizable,
        settingsType: SettingsType,
    ) {
        // TODO CodeStyleSettingsPresentations -> seznam skupin
        if (settingsType == SettingsType.SPACING_SETTINGS) {
        } else if (settingsType == SettingsType.INDENT_SETTINGS) {
            consumer.showAllStandardOptions()
        } else if (settingsType == SettingsType.BLANK_LINES_SETTINGS) {
            consumer.showCustomOption(
                GdCodeStyleSettings::class.java,
                "LINES_BEFORE_FUNC",
                "Before func declaration",
                "Keep maximum blank lines"
            );
            consumer.showCustomOption(
                GdCodeStyleSettings::class.java,
                "LINES_AFTER_HEADER",
                "After class_name/extends header",
                "Keep maximum blank lines"
            );
        }
    }

    override fun getCodeSample(settingsType: SettingsType): String {
        return """extends Node2D
            |class_name MyClass
            |
            |var vari: int = 1;
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
