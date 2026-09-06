package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.model.psi.PsiSymbolReference
import com.intellij.polySymbols.references.polySymbolOwnReferences
import com.intellij.psi.PsiElementVisitor
import gdscript.polySymbols.GdPolySymbolKind.TYPE_HINTS
import gdscript.psi.GdTypeHintRef
import gdscript.psi.GdVisitor
import org.jetbrains.annotations.Unmodifiable

class GdTypeHintRefImpl(node: ASTNode) : GdRefElementImpl(node), GdTypeHintRef {
    fun accept(visitor: GdVisitor) {
        visitor.visitTypeHintRef(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override fun getOwnReferences(): @Unmodifiable Collection<PsiSymbolReference> =
        polySymbolOwnReferences(this) {
            resolveFromNameMatchQuery(TYPE_HINTS, text)
        }
}
