package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdAwaitEx
import gdscript.psi.GdExpr
import gdscript.psi.GdVisitor

class GdAwaitExImpl(node: ASTNode) : GdExprImpl(node), GdAwaitEx {
    override fun accept(visitor: GdVisitor) {
        visitor.visitAwaitEx(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val expr: GdExpr?
        get() = PsiTreeUtil.getChildOfType(this, GdExpr::class.java)
}
