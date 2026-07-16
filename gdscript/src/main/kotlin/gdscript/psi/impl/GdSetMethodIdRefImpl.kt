package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.model.psi.PsiSymbolReference
import com.intellij.polySymbols.references.polySymbolOwnReferences
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.polySymbols.GdPolySymbolKind.METHOD
import gdscript.polySymbols.GdPolySymbolModifier.STATIC
import gdscript.polySymbols.scope.hasModifier
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdSetMethodIdRef
import gdscript.psi.GdVisitor
import org.jetbrains.annotations.Unmodifiable

class GdSetMethodIdRefImpl(node: ASTNode) : GdRefElementImpl(node), GdSetMethodIdRef {
    fun accept(visitor: GdVisitor) {
        visitor.visitSetMethodIdRef(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override fun getOwnReferences(): @Unmodifiable Collection<PsiSymbolReference> =
        polySymbolOwnReferences(this) {
            val requireStatic = PsiTreeUtil.getStubOrPsiParentOfType(
                this@GdSetMethodIdRefImpl, GdClassVarDeclTl::class.java
            )?.isStatic == true
            resolveFromNameMatchQuery(METHOD, text) { symbol ->
                !requireStatic || symbol.hasModifier(STATIC)
            }
        }
}
