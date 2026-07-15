package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdExpr
import gdscript.psi.GdPlusMinusPreEx
import gdscript.psi.GdVisitor

class GdPlusMinusPreExImpl(node: ASTNode) : GdExprImpl(node), GdPlusMinusPreEx {
    override fun accept(visitor: GdVisitor) {
        visitor.visitPlusMinusPreEx(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val expr: GdExpr?
        get() = PsiTreeUtil.getChildOfType(this, GdExpr::class.java)
}
