package gdscript.formatter.settings

import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.formatter.GdCodeStyleSettings
import gdscript.formatter.settings.GdSettings.consume

class GdLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    override fun getLanguage(): Language {
        return GdLanguage
    }

    override fun getIndentOptionsEditor(): IndentOptionsEditor {
        return SmartIndentOptionsEditor()
    }

    override fun customizeDefaults(commonSettings: CommonCodeStyleSettings, indentOptions: CommonCodeStyleSettings.IndentOptions) {
        indentOptions.USE_TAB_CHARACTER = true
        super.customizeDefaults(commonSettings, indentOptions)
    }

    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings =
        GdCodeStyleSettings(settings)

    override fun customizeSettings(
        consumer: CodeStyleSettingsCustomizable,
        settingsType: SettingsType,
    ) {
        when (settingsType) {
            SettingsType.SPACING_SETTINGS -> {
                GdSettings.SPACING.consume(consumer)
            }

            SettingsType.INDENT_SETTINGS -> {
                consumer.showAllStandardOptions()
            }

            SettingsType.BLANK_LINES_SETTINGS -> {
                GdSettings.BLANK_LINES.consume(consumer)
            }

            SettingsType.WRAPPING_AND_BRACES_SETTINGS -> {
                GdSettings.WRAPPING_AND_BRACES.consume(consumer)
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
        // Note to inform about the current formatter status.
        return """# NOTE: GDScript reformat currently converts only indentation (tabs/spaces) according to Code Style.
            |# It does not change other spacing, alignment, or blank lines.
            |# Consider enabling "Main menu | View | Active Editor | Show Whitespaces"
        """.trimMargin()
        //
        //return """extends Node2D
        //    |class_name MyClass
        //    |
        //    |@export
        //    |var vari: int = 1 # Comment
        //    |var var2: String = "" # Comment
        //    |var arrayVar := [
        //    |    {
        //    |        "vb": "asd",
        //    |    },
        //    |]
        //    |const cc = "cc"
        //    |
        //    |func fn1():
        //    |   pass
        //    |
        //    |
        //    |func fn2():
        //    |   pass
        //""".trimMargin()
    }

}
