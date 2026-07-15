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
 * Represents a local variable declared in a GDScript function body.
 *
 * It is local, so has no declaring class.
 */
class GdPsiLocalVariableSymbol(
    override val sourceElement: GdVarNmi
) : GdPsiPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.LOCAL_VARIABLE
    override val name: String get() = sourceElement.name
    override val declaringClassName: String get() = ""
    override val declaringClassId: String get() = ""

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.VAR_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.LOCAL_USER_DEFINED

    override fun createPointer(): Pointer<out GdPsiLocalVariableSymbol> {
        val ptr = sourceElement.createSmartPointer()
        return Pointer { ptr.element?.let { GdPsiLocalVariableSymbol(it) } }
    }
}
