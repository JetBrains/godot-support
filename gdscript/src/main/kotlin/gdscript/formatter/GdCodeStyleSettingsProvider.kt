package gdscript.formatter

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import gdscript.GdLanguage

class GdCodeStyleSettingsProvider : CodeStyleSettingsProvider() {

    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings {
        return GdCodeStyleSettings(settings)
    }

    override fun getConfigurableDisplayName(): String = "GdScript";

    override fun createConfigurable(
        settings: CodeStyleSettings,
        modelSettings: CodeStyleSettings,
    ): CodeStyleConfigurable {
        // TODO
        /*object : CodeStyleAbstractConfigurable(settings, modelSettings, "GdScript") {
            override fun createPanel(settings: CodeStyleSettings?): CodeStyleAbstractPanel {
                return object : TabbedLanguageCodeStylePanel(
                    GdLanguage.INSTANCE,
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
        TabbedLanguageCodeStylePanel(GdLanguage.INSTANCE, currentSettings, settings)

}