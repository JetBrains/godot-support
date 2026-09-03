package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdArrEx
import gdscript.psi.GdExpr
import gdscript.psi.GdVisitor

class GdArrExImpl(node: ASTNode) : GdExprImpl(node), GdArrEx {
    override fun accept(visitor: GdVisitor) {
        visitor.visitArrEx(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val exprList: List<GdExpr>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdExpr::class.java)
}
