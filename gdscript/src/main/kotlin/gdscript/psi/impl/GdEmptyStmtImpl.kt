package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdEmptyStmt
import gdscript.psi.GdEndStmt
import gdscript.psi.GdVisitor

class GdEmptyStmtImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdEmptyStmt {
    fun accept(visitor: GdVisitor) {
        visitor.visitEmptyStmt(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val endStmt: GdEndStmt?
        get() = PsiTreeUtil.getChildOfType(this, GdEndStmt::class.java)
}
