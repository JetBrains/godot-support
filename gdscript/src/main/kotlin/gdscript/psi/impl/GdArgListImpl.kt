package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdArgExpr
import gdscript.psi.GdArgList
import gdscript.psi.GdVisitor

class GdArgListImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdArgList {
    fun accept(visitor: GdVisitor) {
        visitor.visitArgList(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val argExprList: List<GdArgExpr>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdArgExpr::class.java)
}
