package gdscript.polySymbols.psi

import com.intellij.model.Pointer
import com.intellij.model.Symbol
import com.intellij.openapi.project.Project
import com.intellij.platform.backend.navigation.NavigationTarget
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.refactoring.PolySymbolRenameTarget
import com.intellij.polySymbols.utils.PolySymbolDelegate

/**
 * Suppresses [delegate]'s own Go To Declaration/Ctrl+click targets while forwarding every other
 * property (kind, name, gdReturnType, gdHasConstructor, queryScope, isEquivalentTo, ...) unchanged.
 * Used only for the CLASS symbol of a bare built-in-Variant-type constructor call (`Vector2(1, 2)`,
 * never `.new()`): the CLASS symbol must stay the first, unfiltered resolved own-reference so every
 * other consumer (return-type inference, GdParamAnnotator's arg validation,
 * checkBuiltinTypeAssignability - all of which call GdSymbolResolverUtil's `.unwrapAllDelegates()`
 * and land back on the real, unwrapped class symbol) keeps working unchanged - only navigating to
 * the class declaration itself is wrong here; the matching `_init` overload(s), resolved alongside
 * this wrapper as separate symbols, are the only valid navigation target(s).
 */
class GdNavigationSuppressedSymbol(override val delegate: PolySymbol) : PolySymbolDelegate<PolySymbol> {

    override val renameTarget: PolySymbolRenameTarget?
        get() = null

    override fun getNavigationTargets(project: Project): Collection<NavigationTarget> = emptyList()

    override fun isEquivalentTo(symbol: Symbol): Boolean =
        symbol === this
            || delegate.isEquivalentTo(symbol)
            || (symbol is GdNavigationSuppressedSymbol && delegate.isEquivalentTo(symbol.delegate))

    override fun createPointer(): Pointer<out GdNavigationSuppressedSymbol> {
        val delegatePtr = delegate.createPointer()
        return Pointer {
            delegatePtr.dereference()?.let { GdNavigationSuppressedSymbol(it) }
        }
    }
}
