package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.psi.createSmartPointer
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.psi.GdSignalIdNmi
import javax.swing.Icon

class GdPsiSignalSymbol(
    override val linkedElement: GdSignalIdNmi,
) : GdPsiPolySymbol() {
    override val project: Project get() = linkedElement.project

    override val kind: PolySymbolKind get() = GdPolySymbolKind.SIGNAL
    override val declaringClassId: String get() = GdPsiPolySymbolUtil.getOwnerClassId(linkedElement)
    override val declaringClassName: String get() = GdPsiPolySymbolUtil.getLeafName(declaringClassId)

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.SIGNAL_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED

    override fun createPointer(): Pointer<out GdPsiSignalSymbol> {
        val sourcePtr = linkedElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let {
                GdPsiSignalSymbol(it)
            }
        }
    }
}