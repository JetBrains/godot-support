package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.psi.createSmartPointer
import gdscript.GdKeywords
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.psi.GdEnumDeclNmi
import gdscript.psi.GdEnumDeclTl
import javax.swing.Icon

class GdPsiEnumSymbol(
    override val linkedElement: GdEnumDeclNmi,
) : GdPsiPolySymbol() {
    override val project: Project get() = linkedElement.project

    override val kind: PolySymbolKind get() = GdPolySymbolKind.ENUM
    override val declaringClassId: String get() = GdPsiPolySymbolUtil.getOwnerClassId(linkedElement)
    override val declaringClassName: String get() = GdPsiPolySymbolUtil.getLeafName(declaringClassId)

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.ENUM_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED
    override val completionTypeText: String get() = GdKeywords.INT

    override val queryScope: List<PolySymbolScope>
        get() = (linkedElement.parent as? GdEnumDeclTl)
            ?.let { listOfNotNull(GdPsiEnumMemberScope(it)) }
            .orEmpty()

    override fun createPointer(): Pointer<out GdPsiEnumSymbol> {
        val sourcePtr = linkedElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let {
                GdPsiEnumSymbol(it)
            }
        }
    }
}