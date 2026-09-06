package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.psi.createSmartPointer
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.psi.GdSignalIdNmi
import javax.swing.Icon

class GdPsiSignalSymbol(
    override val sourceElement: GdSignalIdNmi,
) : GdPsiPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.SIGNAL
    override val declaringClassId: String get() = GdPsiPolySymbolUtil.getOwnerClassId(sourceElement)
    override val declaringClassName: String get() = GdPsiPolySymbolUtil.getLeafName(declaringClassId)

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.SIGNAL_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED

    override fun createPointer(): Pointer<out GdPsiSignalSymbol> {
        val sourcePtr = sourceElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let {
                GdPsiSignalSymbol(it)
            }
        }
    }
}