package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.polySymbols.references.PolySymbolOwnReferences
import com.intellij.polySymbols.references.PolySymbolOwnReferencesHost
import com.intellij.psi.PsiElementVisitor
import gdscript.polySymbols.GdPolySymbolKind.TYPE_HINTS
import gdscript.psi.GdTypeHintRef
import gdscript.psi.GdVisitor

class GdTypeHintRefImpl(node: ASTNode) : GdRefElementImpl(node), GdTypeHintRef, PolySymbolOwnReferencesHost {
    fun accept(visitor: GdVisitor) {
        visitor.visitTypeHintRef(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override fun buildOwnReferences(builder: PolySymbolOwnReferences.Builder) {
        builder.fromNameMatchQuery(TYPE_HINTS, text)
    }
}
