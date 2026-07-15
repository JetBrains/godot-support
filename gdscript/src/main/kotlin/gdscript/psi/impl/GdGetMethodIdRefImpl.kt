package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.polySymbols.references.PolySymbolOwnReferences
import com.intellij.polySymbols.references.PolySymbolOwnReferencesHost
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.polySymbols.GdPolySymbolKind.METHOD
import gdscript.polySymbols.GdPolySymbolModifier.STATIC
import gdscript.polySymbols.scope.hasModifier
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdGetMethodIdRef
import gdscript.psi.GdVisitor

class GdGetMethodIdRefImpl(node: ASTNode) : GdRefElementImpl(node), GdGetMethodIdRef, PolySymbolOwnReferencesHost {
    fun accept(visitor: GdVisitor) {
        visitor.visitGetMethodIdRef(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override fun buildOwnReferences(builder: PolySymbolOwnReferences.Builder) {
        val requireStatic = PsiTreeUtil.getStubOrPsiParentOfType(this, GdClassVarDeclTl::class.java)?.isStatic == true
        builder.fromNameMatchQuery(METHOD, text) { symbol ->
            !requireStatic || symbol.hasModifier(STATIC)
        }
    }
}
