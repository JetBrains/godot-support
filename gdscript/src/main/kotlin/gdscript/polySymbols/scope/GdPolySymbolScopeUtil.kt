package gdscript.polySymbols.scope

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolModifier
import com.intellij.polySymbols.completion.PolySymbolCodeCompletionItem
import com.intellij.polySymbols.utils.unwrapMatchedSymbols
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolModifier

/**
 * A `nameMatchQuery`/`codeCompletionQuery` result is often a `PolySymbolMatch` wrapper whose own
 * [PolySymbol.modifiers] is always empty — the real modifiers live on the underlying matched
 * symbol(s). Check those instead of `modifiers` directly.
 */
fun PolySymbol.hasModifier(modifier: PolySymbolModifier): Boolean =
    unwrapMatchedSymbols().any { it.modifiers.contains(modifier) }

/**
 * Only methods and properties have a static/instance duality - classes, constructors, constants,
 * signals and enums are always accessible regardless of whether the qualifier looks static, so
 * static-access filtering must not be applied to them (they never carry [GdPolySymbolModifier.STATIC]
 * in the first place, so applying the filter would just make them unresolvable through a qualifier).
 */
private val STATIC_ELIGIBLE_KINDS = setOf(GdPolySymbolKind.METHOD, GdPolySymbolKind.PROPERTY)

fun PolySymbol.hasStaticInstanceDistinction(): Boolean =
    unwrapMatchedSymbols().any { it.kind in STATIC_ELIGIBLE_KINDS }

fun List<PolySymbolCodeCompletionItem>.filterOutNonStaticCompletionItems(): List<PolySymbolCodeCompletionItem> =
    filter { it.symbol?.hasModifier(GdPolySymbolModifier.STATIC) == true }
