package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdElseSt
import gdscript.psi.GdStmtOrSuite
import gdscript.psi.GdVisitor

class GdElseStImpl(node: ASTNode) : GdStmtImpl(node), GdElseSt {
    override fun accept(visitor: GdVisitor) {
        visitor.visitElseSt(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val stmtOrSuite: GdStmtOrSuite?
        get() = PsiTreeUtil.getChildOfType(this, GdStmtOrSuite::class.java)
}
