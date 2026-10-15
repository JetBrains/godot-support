package gdscript.polySymbols.psi

import com.intellij.model.Pointer
import com.intellij.model.Symbol
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.refactoring.PolySymbolRenameTarget
import com.intellij.polySymbols.utils.PolySymbolDelegate
import gdscript.GdKeywords
import gdscript.highlighter.GdHighlighterColors

/**
 * The parent class method that the `super` keyword calls, as the own reference of `super` resolves it.
 *
 * The symbol has the name `super`, because the platform requires the reference range text to match the symbol name.
 * It keeps the keyword color, and it is not a rename target, so a rename from `super` never replaces the keyword.
 * Every other property, the navigation, and the documentation come from [delegate].
 */
class GdSuperSymbol(override val delegate: PolySymbol) : PolySymbolDelegate<PolySymbol> {

    override val name: String
        get() = GdKeywords.SUPER

    override val renameTarget: PolySymbolRenameTarget?
        get() = null

    @PolySymbol.Property(PolySymbol.TextAttributesKeyProperty::class)
    private val textAttributesKey: TextAttributesKey
        get() = GdHighlighterColors.KEYWORD

    override fun isEquivalentTo(symbol: Symbol): Boolean =
        symbol === this
            || delegate.isEquivalentTo(symbol)
            || (symbol is GdSuperSymbol && delegate.isEquivalentTo(symbol.delegate))

    override fun createPointer(): Pointer<out GdSuperSymbol> {
        val delegatePtr = delegate.createPointer()
        return Pointer {
            delegatePtr.dereference()?.let { GdSuperSymbol(it) }
        }
    }
}
