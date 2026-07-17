package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.model.psi.PsiSymbolReference
import com.intellij.polySymbols.references.polySymbolOwnReferences
import com.intellij.psi.PsiElementVisitor
import gdscript.polySymbols.GdPolySymbolKind.INHERITANCE_SYMBOLS
import gdscript.psi.GdInheritanceSubIdRef
import gdscript.psi.GdVisitor
import org.jetbrains.annotations.Unmodifiable

class GdInheritanceSubIdRefImpl(node: ASTNode) : GdRefElementImpl(node), GdInheritanceSubIdRef {
    fun accept(visitor: GdVisitor) {
        visitor.visitInheritanceSubIdRef(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override fun getOwnReferences(): @Unmodifiable Collection<PsiSymbolReference> =
        polySymbolOwnReferences(this) {
            resolveFromNameMatchQuery(INHERITANCE_SYMBOLS, text)
        }
}
