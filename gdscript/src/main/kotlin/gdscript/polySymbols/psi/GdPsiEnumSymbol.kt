package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
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
    override val sourceElement: GdEnumDeclNmi,
) : GdPsiPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.ENUM
    override val declaringClassId: String get() = GdPsiPolySymbolUtil.getOwnerClassId(sourceElement)
    override val declaringClassName: String get() = GdPsiPolySymbolUtil.getLeafName(declaringClassId)

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.ENUM_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED
    override val completionTypeText: String get() = GdKeywords.INT

    override val queryScope: List<PolySymbolScope>
        get() = (sourceElement.parent as? GdEnumDeclTl)
            ?.let { listOfNotNull(gdPsiEnumMemberScope(it)) }
            .orEmpty()

    override fun createPointer(): Pointer<out GdPsiEnumSymbol> {
        val sourcePtr = sourceElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let {
                GdPsiEnumSymbol(it)
            }
        }
    }
}