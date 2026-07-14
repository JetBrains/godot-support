package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
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
    override val linkedElement: GdVarNmi,
) : GdPsiPolySymbol() {
    override val project: Project get() = linkedElement.project

    override val kind: PolySymbolKind get() = GdPolySymbolKind.PROPERTY
    override val declaringClassId: String get() = GdPsiPolySymbolUtil.getOwnerClassId(linkedElement)
    override val declaringClassName: String get() = GdPsiPolySymbolUtil.getLeafName(declaringClassId)

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.VAR_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED
    override val completionTypeText: String? get() = (linkedElement.parent as? GdClassVarDeclTl)?.returnType?.takeIf { it.isNotEmpty() }

    override val modifiers: Set<PolySymbolModifier>
        get() = if ((linkedElement.parent as? GdClassVarDeclTl)?.isStatic == true) setOf(GdPolySymbolModifier.STATIC) else emptySet()

    override fun createPointer(): Pointer<out GdPsiPropertySymbol> {
        val sourcePtr = linkedElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let {
                GdPsiPropertySymbol(it)
            }
        }
    }
}