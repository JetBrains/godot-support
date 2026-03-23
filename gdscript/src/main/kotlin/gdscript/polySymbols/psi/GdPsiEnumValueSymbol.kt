package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.psi.createSmartPointer
import gdscript.GdKeywords
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.psi.GdEnumValueNmi
import javax.swing.Icon

/**
 * Poly symbol for a single enum value (e.g. `VALUE1` in `enum MyEnum { VALUE1, VALUE2 }`).
 * Enum values are not indexed separately; they live inside their containing enum's body.
 *
 * [ownerClassId] identifies the class that owns the enum (not the enum itself).
 */
class GdPsiEnumValueSymbol(
    override val linkedElement: GdEnumValueNmi,
) : GdPsiPolySymbol() {
    override val project: Project get() = linkedElement.project

    // TODO should probably add a reference to the enum that owns it, but havent found a need for it yet
    override val kind: PolySymbolKind get() = GdPolySymbolKind.ENUM_VALUE
    override val declaringClassId: String get() = GdPsiPolySymbolUtil.getOwnerClassId(linkedElement)
    override val declaringClassName: String get() = GdPsiPolySymbolUtil.getLeafName(declaringClassId)

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.ENUM_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED
    override val completionTypeText: String get() = GdKeywords.INT

    override fun createPointer(): Pointer<out GdPsiEnumValueSymbol> {
        val sourcePtr = linkedElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let { GdPsiEnumValueSymbol(it) }
        }
    }
}
