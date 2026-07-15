package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdExpr
import gdscript.psi.GdTernaryEx
import gdscript.psi.GdVisitor

class GdTernaryExImpl(node: ASTNode) : GdExprImpl(node), GdTernaryEx {
    override fun accept(visitor: GdVisitor) {
        visitor.visitTernaryEx(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val exprList: List<GdExpr>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdExpr::class.java)
}
