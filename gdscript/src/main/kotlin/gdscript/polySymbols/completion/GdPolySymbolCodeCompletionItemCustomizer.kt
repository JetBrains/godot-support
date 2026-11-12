package gdscript.polySymbols.completion

import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.completion.PolySymbolCodeCompletionItem
import com.intellij.polySymbols.completion.PolySymbolCodeCompletionItemCustomizer
import com.intellij.polySymbols.context.PolyContext
import com.intellij.psi.PsiElement
import gdscript.polySymbols.gdCompletionTailText
import gdscript.polySymbols.gdCompletionTypeText

class GdPolySymbolCodeCompletionItemCustomizer : PolySymbolCodeCompletionItemCustomizer {
    override fun customize(
        item: PolySymbolCodeCompletionItem,
        context: PolyContext,
        kind: PolySymbolKind,
        location: PsiElement,
    ): PolySymbolCodeCompletionItem {
        // The platform calls each customizer for the items of all plugins, for example HTML or Angular.
        // Keep the text of an item when the symbol has no GDScript text.
        val symbol = item.symbol ?: return item
        val tailText = symbol.gdCompletionTailText
        val typeText = symbol.gdCompletionTypeText
        var result = item
        if (tailText != null) result = result.withTailText(tailText)
        if (typeText != null) result = result.withTypeText(typeText)
        return result
    }
}
