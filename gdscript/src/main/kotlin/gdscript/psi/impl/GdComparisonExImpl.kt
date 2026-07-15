package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdComparisonEx
import gdscript.psi.GdExpr
import gdscript.psi.GdOperator
import gdscript.psi.GdVisitor

class GdComparisonExImpl(node: ASTNode) : GdExprImpl(node), GdComparisonEx {
    override fun accept(visitor: GdVisitor) {
        visitor.visitComparisonEx(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val exprList: List<GdExpr>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdExpr::class.java)

    override val operator: GdOperator
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdOperator::class.java))
}
