package gdscript.psi

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.references.PolySymbolOwnReferences
import com.intellij.polySymbols.references.PolySymbolOwnReferencesHost
import gdscript.polySymbols.GdPolySymbolKind.QUALIFIABLE_SYMBOLS
import gdscript.polySymbols.GdPolySymbolModifier.STATIC
import gdscript.polySymbols.psi.GdPsiPolySymbolUtil.isStatic

interface GdRefIdRef : GdRefElement, PolySymbolOwnReferencesHost {
    override fun buildOwnReferences(builder: PolySymbolOwnReferences.Builder) {
        builder.fromNameMatchQuery(
            QUALIFIABLE_SYMBOLS, getText(),
            if (isStatic(this))
                { symbol: PolySymbol? -> symbol!!.modifiers.contains(STATIC) }
            else
                { `_`: PolySymbol? -> true }
        )
    }
}
