package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdExpr
import gdscript.psi.GdForSt
import gdscript.psi.GdStmtOrSuite
import gdscript.psi.GdTyped
import gdscript.psi.GdVarNmi
import gdscript.psi.GdVisitor

class GdForStImpl(node: ASTNode) : GdStmtImpl(node), GdForSt {
    override fun accept(visitor: GdVisitor) {
        visitor.visitForSt(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val expr: GdExpr?
        get() = PsiTreeUtil.getChildOfType(this, GdExpr::class.java)

    override val stmtOrSuite: GdStmtOrSuite?
        get() = PsiTreeUtil.getChildOfType(this, GdStmtOrSuite::class.java)

    override val varNmi: GdVarNmi?
        get() = PsiTreeUtil.getChildOfType(this, GdVarNmi::class.java)

    override val typed: GdTyped?
        get() = PsiTreeUtil.getChildOfType(this, GdTyped::class.java)
}
