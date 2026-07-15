package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdArgExpr
import gdscript.psi.GdExpr
import gdscript.psi.GdPsiUtils.getReturnType
import gdscript.psi.GdVisitor

class GdArgExprImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdArgExpr {
    fun accept(visitor: GdVisitor) {
        visitor.visitArgExpr(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val expr: GdExpr
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdExpr::class.java))

    override val returnType: String
        get() = getReturnType(this)
}
