package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdExpr
import gdscript.psi.GdPlusMinusEx
import gdscript.psi.GdVisitor

class GdPlusMinusExImpl(node: ASTNode) : GdExprImpl(node), GdPlusMinusEx {
    override fun accept(visitor: GdVisitor) {
        visitor.visitPlusMinusEx(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val expr: GdExpr
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdExpr::class.java))
}
