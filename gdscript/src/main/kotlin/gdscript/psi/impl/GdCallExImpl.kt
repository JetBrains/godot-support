package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdArgList
import gdscript.psi.GdCallEx
import gdscript.psi.GdExpr
import gdscript.psi.GdVisitor

class GdCallExImpl(node: ASTNode) : GdExprImpl(node), GdCallEx {
    override fun accept(visitor: GdVisitor) {
        visitor.visitCallEx(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val argList: GdArgList?
        get() = PsiTreeUtil.getChildOfType(this, GdArgList::class.java)

    override val expr: GdExpr
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdExpr::class.java))
}
