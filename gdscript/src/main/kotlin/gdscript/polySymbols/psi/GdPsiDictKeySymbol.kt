package gdscript.polySymbols.psi

import GdScriptPluginIcons
import com.intellij.model.Pointer
import com.intellij.openapi.util.TextRange
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.psi.createSmartPointer
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.polySymbols.psi.GdPsiPolySymbolUtil.quotedContentRange
import gdscript.psi.GdKeyNmi
import javax.swing.Icon

/**
 * A dictionary literal's key (`{"key1": 1}`/`{key1 = 1}`), reachable via attribute access on the
 * dictionary (`dict.key1`). It is local to wherever the dictionary literal is declared, so has no
 * declaring class.
 */
class GdPsiDictKeySymbol(
    override val sourceElement: GdKeyNmi
) : GdPsiPolySymbol() {
    override val kind: PolySymbolKind get() = GdPolySymbolKind.DICT_KEY
    override val declaringClassName: String get() = ""
    override val declaringClassId: String get() = ""

    override val icon: Icon get() = GdScriptPluginIcons.GDScriptIcons.VAR_MARKER
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.LOCAL_USER_DEFINED

    // A string-literal key's own text keeps its surrounding quotes (e.g. `"key1"`), which aren't
    // part of the declared name (GdKeyNmi.getName() strips them) - excluding them here keeps
    // rename from replacing the quotes themselves along with the name.
    override val textRangeInSourceElement: TextRange
        get() = quotedContentRange(sourceElement.text)

    override fun createPointer(): Pointer<out GdPsiDictKeySymbol> {
        val ptr = sourceElement.createSmartPointer()
        return Pointer { ptr.element?.let { GdPsiDictKeySymbol(it) } }
    }
}
