package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdAssignSign
import gdscript.psi.GdVisitor

class GdAssignSignImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdAssignSign {
    fun accept(visitor: GdVisitor) {
        visitor.visitAssignSign(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
