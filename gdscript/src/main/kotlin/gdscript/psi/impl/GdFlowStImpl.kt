package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdEndStmt
import gdscript.psi.GdExpr
import gdscript.psi.GdFlowSt
import gdscript.psi.GdPsiUtils.getType
import gdscript.psi.GdVisitor

class GdFlowStImpl(node: ASTNode) : GdStmtImpl(node), GdFlowSt {
    override fun accept(visitor: GdVisitor) {
        visitor.visitFlowSt(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val endStmt: GdEndStmt
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdEndStmt::class.java))

    override val expr: GdExpr?
        get() = PsiTreeUtil.getChildOfType(this, GdExpr::class.java)

    override val type: String
        get() = getType(this)
}
