package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdStmt
import gdscript.psi.GdVisitor

abstract class GdStmtImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdStmt {
    open fun accept(visitor: GdVisitor) {
        visitor.visitStmt(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
