package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdExpr
import gdscript.psi.GdStmtOrSuite
import gdscript.psi.GdVisitor
import gdscript.psi.GdWhileSt

class GdWhileStImpl(node: ASTNode) : GdStmtImpl(node), GdWhileSt {
    override fun accept(visitor: GdVisitor) {
        visitor.visitWhileSt(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val expr: GdExpr?
        get() = PsiTreeUtil.getChildOfType(this, GdExpr::class.java)

    override val stmtOrSuite: GdStmtOrSuite?
        get() = PsiTreeUtil.getChildOfType(this, GdStmtOrSuite::class.java)
}
