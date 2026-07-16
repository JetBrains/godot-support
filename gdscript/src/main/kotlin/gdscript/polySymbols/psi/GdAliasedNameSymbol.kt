package gdscript.polySymbols.psi

import com.intellij.model.Pointer
import com.intellij.model.Symbol
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.refactoring.PolySymbolRenameTarget
import com.intellij.polySymbols.utils.PolySymbolDelegate

/**
 * Reports [name] as its own name while delegating everything else (declaration, search
 * targets, resolution equivalence, query scope) to [delegate].
 *
 * Needed wherever a query is resolved by one piece of text to a symbol whose own declared name is
 * different - e.g. `ClassName.new(...)`'s `new` token resolving to the class's `_init` constructor
 * symbol, or a resource-path inheritance clause (`extends "res://base.gd"`) resolving to the
 * target file's `class_name`-declared class symbol, whose own name is the class name, not the
 * resource path.
 */
class GdAliasedNameSymbol(
    override val delegate: PolySymbol,
    override val name: String,
) : PolySymbolDelegate<PolySymbol> {

    override val renameTarget: PolySymbolRenameTarget?
        get() = null

    override fun isEquivalentTo(symbol: Symbol): Boolean =
        symbol === this
            || delegate.isEquivalentTo(symbol)
            || (symbol is GdAliasedNameSymbol && delegate.isEquivalentTo(symbol.delegate))

    @Suppress("UNCHECKED_CAST")
    override fun createPointer(): Pointer<out GdAliasedNameSymbol> {
        val delegatePtr = delegate.createPointer()
        val aliasName = name
        return Pointer {
            delegatePtr.dereference()?.let { GdAliasedNameSymbol(it, aliasName) }
        }
    }
}
