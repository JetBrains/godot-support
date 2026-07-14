package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.PolySymbolModifier
import com.intellij.psi.createSmartPointer
import gdscript.completion.utils.GdMethodCompletionUtil.buildParamHint
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolModifier
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi
import javax.swing.Icon

class GdPsiMethodSymbol(
    override val linkedElement: GdMethodIdNmi,
) : GdPsiPolySymbol() {
    override val project: Project get() = linkedElement.project

    override val kind: PolySymbolKind get() = GdPolySymbolKind.METHOD
    override val declaringClassId: String get() = GdPsiPolySymbolUtil.getOwnerClassId(linkedElement)
    override val declaringClassName: String get() = GdPsiPolySymbolUtil.getLeafName(declaringClassId)

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.METHOD_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED
    override val completionTailText: String? get() = (linkedElement.parent as? GdMethodDeclTl)?.let { buildParamHint(it) }
    override val completionTypeText: String? get() = (linkedElement.parent as? GdMethodDeclTl)?.returnType?.takeIf { it.isNotEmpty() }

    override val modifiers: Set<PolySymbolModifier>
        get() = if ((linkedElement.parent as? GdMethodDeclTl)?.isStatic == true) setOf(GdPolySymbolModifier.STATIC) else emptySet()

    override fun createPointer(): Pointer<out GdPsiMethodSymbol> {
        val sourcePtr = linkedElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let {
                GdPsiMethodSymbol(it)
            }
        }
    }
}