package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdStmt
import gdscript.psi.GdSuite
import gdscript.psi.GdVisitor

class GdSuiteImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdSuite {
    fun accept(visitor: GdVisitor) {
        visitor.visitSuite(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val stmtList: List<GdStmt>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdStmt::class.java)
}
