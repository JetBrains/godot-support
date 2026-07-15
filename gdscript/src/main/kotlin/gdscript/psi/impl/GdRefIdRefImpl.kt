package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.polySymbols.references.PolySymbolOwnReferences
import com.intellij.polySymbols.references.PolySymbolOwnReferencesHost
import com.intellij.psi.PsiElementVisitor
import gdscript.polySymbols.GdPolySymbolKind.QUALIFIABLE_SYMBOLS
import gdscript.polySymbols.GdPolySymbolModifier.STATIC
import gdscript.polySymbols.psi.GdPsiPolySymbolUtil.isStatic
import gdscript.polySymbols.psi.GdPsiPolySymbolUtil.resolveConstructorSymbol
import gdscript.polySymbols.scope.hasModifier
import gdscript.polySymbols.scope.hasStaticInstanceDistinction
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdVisitor

class GdRefIdRefImpl(node: ASTNode) : GdRefElementImpl(node), GdRefIdRef, PolySymbolOwnReferencesHost {
    fun accept(visitor: GdVisitor) {
        visitor.visitRefIdNm(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override fun buildOwnReferences(builder: PolySymbolOwnReferences.Builder) {
        if (text == "new") {
            resolveConstructorSymbol(this)?.let { builder.reference(it) }
            return
        }
        val requireStatic = isStatic(this)
        builder.fromNameMatchQuery(QUALIFIABLE_SYMBOLS, text) { symbol ->
            !requireStatic || !symbol.hasStaticInstanceDistinction() || symbol.hasModifier(STATIC)
        }
    }
}
