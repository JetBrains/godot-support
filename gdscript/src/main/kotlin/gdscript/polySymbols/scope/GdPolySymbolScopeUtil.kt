package gdscript.polySymbols.scope

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.completion.PolySymbolCodeCompletionItem
import gdscript.polySymbols.GdPolySymbolModifier


fun List<PolySymbol>.filterOutNonStatic(): List<PolySymbol> =
    filter { it.modifiers.contains(GdPolySymbolModifier.STATIC) }

fun List<PolySymbolCodeCompletionItem>.filterOutNonStaticCompletionItems(): List<PolySymbolCodeCompletionItem> =
    filter { it.symbol?.modifiers?.contains(GdPolySymbolModifier.STATIC) == true }
