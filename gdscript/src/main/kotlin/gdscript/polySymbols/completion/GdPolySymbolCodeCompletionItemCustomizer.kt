package gdscript.polySymbols.completion

import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.completion.PolySymbolCodeCompletionItem
import com.intellij.polySymbols.completion.PolySymbolCodeCompletionItemCustomizer
import com.intellij.polySymbols.context.PolyContext
import com.intellij.psi.PsiElement
import gdscript.polySymbols.GdPolySymbol

class GdPolySymbolCodeCompletionItemCustomizer : PolySymbolCodeCompletionItemCustomizer {
    override fun customize(
        item: PolySymbolCodeCompletionItem,
        context: PolyContext,
        kind: PolySymbolKind,
        location: PsiElement,
    ): PolySymbolCodeCompletionItem {
        val symbol = item.symbol as? GdPolySymbol ?: return item
        return item
            .withTailText(symbol.completionTailText)
            .withTypeText(symbol.completionTypeText)
    }
}
