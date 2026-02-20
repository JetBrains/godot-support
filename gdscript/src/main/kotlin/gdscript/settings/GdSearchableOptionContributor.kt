package gdscript.settings

import com.intellij.ide.ui.search.SearchableOptionContributor
import com.intellij.ide.ui.search.SearchableOptionProcessor
import gdscript.GdScriptBundle

internal class GdSearchableOptionContributor : SearchableOptionContributor() {
    override fun processOptions(processor: SearchableOptionProcessor) {
        val configurableId = "gdscript.settings.GdSettingsConfigurable"
        val displayName = GdScriptBundle.message("language.name")

        processor.addOptions("Godot", null, null, configurableId, displayName, false)
    }
}
