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
        val symbol = item.symbol ?: return item
        return item
            .withTailText(symbol.gdCompletionTailText)
            .withTypeText(symbol.gdCompletionTypeText)
    }
}
