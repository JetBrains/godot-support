package gdscript.formatter

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.jetbrains.rider.godot.community.gdscript.GdLanguage

// TODO is this needed?
class GdCodeStyleSettingsProvider(private val language: Language = GdLanguage) : CodeStyleSettingsProvider() {

    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings {
        return GdCodeStyleSettings(settings)
    }

    override fun getLanguage(): Language? {
        return language
    }

    override fun createConfigurable(
        settings: CodeStyleSettings,
        modelSettings: CodeStyleSettings,
    ): CodeStyleConfigurable {
        // TODO
        /*object : CodeStyleAbstractConfigurable(settings, modelSettings, "GdScript") {
            override fun createPanel(settings: CodeStyleSettings?): CodeStyleAbstractPanel {
                return object : TabbedLanguageCodeStylePanel(
                    GdLanguage,
                    currentSettings,
                    settings
                ) {
                    override fun initTabs(settings: CodeStyleSettings) {
                        add
                        addBlankLinesTab(settings);
                    }
                }
            }
        }*/

        return object : CodeStyleAbstractConfigurable(settings, modelSettings, this.configurableDisplayName) {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {
                return GdCodeStyleMainPanel(currentSettings, settings)
            }
        }
    }

    private class GdCodeStyleMainPanel(currentSettings: CodeStyleSettings, settings: CodeStyleSettings) :
        TabbedLanguageCodeStylePanel(GdLanguage, currentSettings, settings)
}
