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
 * Represents a method parameter in GDScript.
 *
 * The [sourceElement] is the [GdVarNmi] name identifier child of [gdscript.psi.GdParam].
 *
 * It is local, so has no declaring class.
 */
class GdPsiParameterSymbol(
    override val sourceElement: GdVarNmi
) : GdPsiPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.PARAMETER
    override val name: String get() = sourceElement.name
    override val declaringClassName: String get() = ""
    override val declaringClassId: String get() = ""

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.VAR_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.LOCAL_USER_DEFINED

    override fun createPointer(): Pointer<out GdPsiParameterSymbol> {
        val ptr = sourceElement.createSmartPointer()
        return Pointer { ptr.element?.let { GdPsiParameterSymbol(it) } }
    }
}
