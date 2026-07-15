package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdElifSt
import gdscript.psi.GdElseSt
import gdscript.psi.GdExpr
import gdscript.psi.GdIfSt
import gdscript.psi.GdStmtOrSuite
import gdscript.psi.GdVisitor

class GdIfStImpl(node: ASTNode) : GdStmtImpl(node), GdIfSt {
    override fun accept(visitor: GdVisitor) {
        visitor.visitIfSt(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val elifStList: List<GdElifSt>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdElifSt::class.java)

    override val elseSt: GdElseSt?
        get() = PsiTreeUtil.getChildOfType(this, GdElseSt::class.java)

    override val expr: GdExpr?
        get() = PsiTreeUtil.getChildOfType(this, GdExpr::class.java)

    override val stmtOrSuite: GdStmtOrSuite?
        get() = PsiTreeUtil.getChildOfType(this, GdStmtOrSuite::class.java)
}
