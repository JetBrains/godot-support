package gdscript.polySymbols.psi

import com.intellij.model.Pointer
import com.intellij.openapi.util.TextRange
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.psi.createSmartPointer
import gdscript.GdIcon
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.utils.VirtualFileUtil.resourcePath
import tscn.psi.TscnNodeHeader
import javax.swing.Icon

/**
 * Poly symbol for a `.tscn` scene node, resolved from `.gd` via `$Node`/`%UniqueNode`.
 *
 * [sourceElement] is a `tscn.psi.TscnNodeHeader`, not a `gdscript.psi.GdNamedElement`, so [name] and
 * [returnType] are overridden - mirrors [GdPsiResourceClassSymbol]'s own override for a source with
 * no owning GDScript class.
 */
class GdPsiNodeSymbol(
    override val sourceElement: TscnNodeHeader,
) : GdPsiPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.NODE
    override val name: String get() = sourceElement.name
    override val declaringClassName: String get() = sceneResourcePath
    override val declaringClassId: String get() = sceneResourcePath
    override val returnType: String get() = sourceElement.type

    override val icon: Icon get() = GdIcon.getEditorIcon(sourceElement.type)
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.USER_DEFINED

    // No dedicated name-identifier leaf on TscnNodeHeader today - an empty range keeps this symbol
    // out of offset-based "what's under the caret" scans, same as GdPsiResourceClassSymbol.
    override val textRangeInSourceElement: TextRange? get() = TextRange.EMPTY_RANGE

    private val sceneResourcePath: String
        get() = (sourceElement.containingFile.virtualFile ?: sourceElement.containingFile.originalFile.virtualFile)
            ?.resourcePath()
            ?: sourceElement.containingFile.name

    override fun createPointer(): Pointer<out GdPsiNodeSymbol> {
        val sourcePtr = sourceElement.createSmartPointer()
        return Pointer {
            sourcePtr.element?.let { GdPsiNodeSymbol(it) }
        }
    }
}
