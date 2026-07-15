package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdOperator
import gdscript.psi.GdVisitor

class GdOperatorImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdOperator {
    fun accept(visitor: GdVisitor) {
        visitor.visitOperator(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
