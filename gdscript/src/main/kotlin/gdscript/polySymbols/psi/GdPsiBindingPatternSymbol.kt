package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.psi.createSmartPointer
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.psi.GdVarNmi
import javax.swing.Icon

/**
 * Represents a variable introduced by a binding pattern in a `match` statement.
 *
 * Example:
 * ```gdscript
 * match value:
 *     var x:  # <- 'x' is a binding pattern variable
 *         print(x)
 * ```
 *
 * The [sourceElement] is the [GdVarNmi] name identifier child of [gdscript.psi.GdBindingPattern].
 *
 * It is local, so has no declaring class.
 */
class GdPsiBindingPatternSymbol(
    override val sourceElement: GdVarNmi
) : GdPsiPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.BINDING_PATTERN
    override val name: String get() = sourceElement.name
    override val declaringClassName: String get() = ""
    override val declaringClassId: String get() = ""

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.VAR_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.LOCAL_USER_DEFINED

    override fun createPointer(): Pointer<out GdPsiBindingPatternSymbol> {
        val ptr = sourceElement.createSmartPointer()
        return Pointer { ptr.element?.let { GdPsiBindingPatternSymbol(it) } }
    }
}
