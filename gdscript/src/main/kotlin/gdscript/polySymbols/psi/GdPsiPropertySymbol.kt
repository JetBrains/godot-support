package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.PolySymbolModifier
import com.intellij.psi.createSmartPointer
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolModifier
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdVarNmi
import javax.swing.Icon

class GdPsiPropertySymbol(
    override val sourceElement: GdVarNmi,
) : GdPsiPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.PROPERTY
    override val declaringClassId: String get() = GdPsiPolySymbolUtil.getOwnerClassId(sourceElement)
    override val declaringClassName: String get() = GdPsiPolySymbolUtil.getLeafName(declaringClassId)

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.VAR_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED
    override val completionTypeText: String? get() = (sourceElement.parent as? GdClassVarDeclTl)?.returnType?.takeIf { it.isNotEmpty() }

    override val modifiers: Set<PolySymbolModifier>
        get() = if ((sourceElement.parent as? GdClassVarDeclTl)?.isStatic == true) setOf(GdPolySymbolModifier.STATIC) else emptySet()

    override fun createPointer(): Pointer<out GdPsiPropertySymbol> {
        val sourcePtr = sourceElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let {
                GdPsiPropertySymbol(it)
            }
        }
    }
}