package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.psi.createSmartPointer
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.psi.GdVarNmi
import javax.swing.Icon

/**
 * Represents a local constant declared in a GDScript function body.
 *
 * It is local, so has no declaring class.
 */
class GdPsiLocalConstantSymbol(
    override val linkedElement: GdVarNmi
) : GdPsiPolySymbol() {
    override val project: Project get() = linkedElement.project

    override val kind: PolySymbolKind get() = GdPolySymbolKind.LOCAL_CONSTANT
    override val name: String get() = linkedElement.name
    override val declaringClassName: String get() = ""
    override val declaringClassId: String get() = ""

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.VAR_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.LOCAL_USER_DEFINED

    override fun createPointer(): Pointer<out GdPsiLocalConstantSymbol> {
        val ptr = linkedElement.createSmartPointer()
        return Pointer { ptr.element?.let { GdPsiLocalConstantSymbol(it) } }
    }
}
