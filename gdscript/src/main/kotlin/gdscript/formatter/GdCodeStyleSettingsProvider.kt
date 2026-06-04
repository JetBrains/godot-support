package gdscript.formatter

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.application.options.codeStyle.OptionTreeWithPreviewPanel
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.GdScriptBundle

class GdCodeStyleSettingsProvider(private val language: Language = GdLanguage) : CodeStyleSettingsProvider() {

    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings {
        return GdCodeStyleSettings(settings)
    }

    override fun getLanguage(): Language {
        return language
    }

    override fun createConfigurable(
        settings: CodeStyleSettings,
        modelSettings: CodeStyleSettings,
    ): CodeStyleConfigurable {
        return object : CodeStyleAbstractConfigurable(settings, modelSettings, this.configurableDisplayName) {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {
                return GdCodeStyleMainPanel(currentSettings, settings)
            }
        }
    }

    private class GdCodeStyleMainPanel(currentSettings: CodeStyleSettings, settings: CodeStyleSettings) :
        TabbedLanguageCodeStylePanel(GdLanguage, currentSettings, settings) {
        override fun initTabs(settings: CodeStyleSettings) {
            super.initTabs(settings)
            addTab(GdLanguageSpecificPanel(settings))
        }
    }

    private class GdLanguageSpecificPanel(settings: CodeStyleSettings) : OptionTreeWithPreviewPanel(settings) {
        init {
            init()
        }

        override fun initTables() {
            // Promote the LANGUAGE_SPECIFIC custom options registered by
            // GdLanguageCodeStyleSettingsProvider into the option tree. The group names must match
            // those used there (same bundle keys), otherwise the options stay hidden.
            initCustomOptions(GdScriptBundle.message("settings.formatter.group.code.insight"))
            initCustomOptions(GdScriptBundle.message("settings.formatter.group.continuation.indents"))
        }

        override fun getSettingsType(): LanguageCodeStyleSettingsProvider.SettingsType =
            LanguageCodeStyleSettingsProvider.SettingsType.LANGUAGE_SPECIFIC

        override fun getDefaultLanguage(): Language = GdLanguage
    }
}
