package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdEndStmt
import gdscript.psi.GdNewLineEnd
import gdscript.psi.GdVisitor

class GdEndStmtImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdEndStmt {
    fun accept(visitor: GdVisitor) {
        visitor.visitEndStmt(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val newLineEnd: GdNewLineEnd?
        get() = PsiTreeUtil.getChildOfType(this, GdNewLineEnd::class.java)
}
