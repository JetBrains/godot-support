package gdscript.polySymbols.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.highlighting.PolySymbolHighlightingCustomizer
import com.intellij.polySymbols.utils.unwrapMatchedSymbols
import com.intellij.psi.PsiElement
import gdscript.GdKeywords
import gdscript.highlighter.GdHighlighterColors
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolModifier
import gdscript.polySymbols.gdDeclaringClassId
import gdscript.polySymbols.gdIsEngineSymbol
import gdscript.utils.PsiElementUtil.getCallExpr

class GdPolySymbolHighlightingCustomizer : PolySymbolHighlightingCustomizer {

    override fun getSymbolTextAttributes(host: PsiElement, symbol: PolySymbol, level: Int): TextAttributesKey? {
        // `symbol` is often a raw nameMatchQuery result wrapper whose own kind/modifiers/etc. don't
        // reflect the real underlying symbol - unwrap first (same pattern as PolySymbol.hasModifier).
        val real = symbol.unwrapMatchedSymbols().firstOrNull() ?: return null

        var attribute = when (real.kind) {
            GdPolySymbolKind.CONSTRUCTOR -> GdHighlighterColors.METHOD_CALL

            GdPolySymbolKind.METHOD -> {
                if (real.gdDeclaringClassId == GdKeywords.GLOBAL_SCOPE) GdHighlighterColors.GLOBAL_FUNCTION
                else if (real.modifiers.contains(GdPolySymbolModifier.STATIC)) GdHighlighterColors.STATIC_METHOD_CALL
                else GdHighlighterColors.METHOD_CALL
            }

            GdPolySymbolKind.PROPERTY -> {
                if (real.gdDeclaringClassId == GdKeywords.GLOBAL_SCOPE) GdHighlighterColors.GLOBAL_VARIABLE_BUILT_IN
                else GdHighlighterColors.MEMBER
            }

            GdPolySymbolKind.CONSTANT,
            GdPolySymbolKind.SIGNAL,
            GdPolySymbolKind.ENUM,
            GdPolySymbolKind.ENUM_VALUE -> GdHighlighterColors.MEMBER

            GdPolySymbolKind.CLASS -> {
                if (real.gdIsEngineSymbol) GdHighlighterColors.ENGINE_TYPE
                else GdHighlighterColors.CLASS_TYPE
            }

            GdPolySymbolKind.AUTOLOAD -> GdHighlighterColors.GLOBAL_VARIABLE_AUTOLOAD
            GdPolySymbolKind.LOADED_CLASS_ALIAS -> GdHighlighterColors.CLASS_TYPE

            else -> GdHighlighterColors.MEMBER
        }

        if (attribute == GdHighlighterColors.MEMBER && host.getCallExpr() != null) {
            attribute = GdHighlighterColors.METHOD_CALL
        }

        return attribute
    }
}
