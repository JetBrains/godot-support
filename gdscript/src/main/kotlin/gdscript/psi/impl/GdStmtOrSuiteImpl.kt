package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdStmt
import gdscript.psi.GdStmtOrSuite
import gdscript.psi.GdSuite
import gdscript.psi.GdVisitor

class GdStmtOrSuiteImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdStmtOrSuite {
    fun accept(visitor: GdVisitor) {
        visitor.visitStmtOrSuite(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val stmt: GdStmt?
        get() = PsiTreeUtil.getChildOfType(this, GdStmt::class.java)

    override val suiteList: List<GdSuite>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdSuite::class.java)
}
