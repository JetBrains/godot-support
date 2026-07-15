package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdAssignTyped
import gdscript.psi.GdVisitor

class GdAssignTypedImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdAssignTyped {
    fun accept(visitor: GdVisitor) {
        visitor.visitAssignTyped(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
