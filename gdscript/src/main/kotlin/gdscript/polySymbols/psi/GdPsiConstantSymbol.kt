package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.psi.createSmartPointer
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdVarNmi
import javax.swing.Icon

class GdPsiConstantSymbol(
    override val sourceElement: GdVarNmi,
) : GdPsiPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.CONSTANT
    override val declaringClassId: String get() = GdPsiPolySymbolUtil.getOwnerClassId(sourceElement)
    override val declaringClassName: String get() = GdPsiPolySymbolUtil.getLeafName(declaringClassId)

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.CONST_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED
    override val completionTypeText: String? get() = (sourceElement.parent as? GdConstDeclTl)?.returnType?.takeIf { it.isNotEmpty() }

    override fun createPointer(): Pointer<out GdPsiConstantSymbol> {
        val sourcePtr = sourceElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let {
                GdPsiConstantSymbol(it)
            }
        }
    }
}