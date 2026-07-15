package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdEndStmt
import gdscript.psi.GdExpr
import gdscript.psi.GdExprSt
import gdscript.psi.GdVisitor

class GdExprStImpl(node: ASTNode) : GdStmtImpl(node), GdExprSt {
    override fun accept(visitor: GdVisitor) {
        visitor.visitExprSt(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val endStmt: GdEndStmt
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdEndStmt::class.java))

    override val expr: GdExpr
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdExpr::class.java))
}
