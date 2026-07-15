package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdAssignSign
import gdscript.psi.GdAssignSt
import gdscript.psi.GdEndStmt
import gdscript.psi.GdExpr
import gdscript.psi.GdVisitor

class GdAssignStImpl(node: ASTNode) : GdStmtImpl(node), GdAssignSt {
    override fun accept(visitor: GdVisitor) {
        visitor.visitAssignSt(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val assignSign: GdAssignSign
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdAssignSign::class.java))

    override val endStmt: GdEndStmt
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdEndStmt::class.java))

    override val exprList: List<GdExpr>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdExpr::class.java)
}
