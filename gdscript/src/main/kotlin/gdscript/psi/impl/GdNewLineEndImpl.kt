package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdNewLineEnd
import gdscript.psi.GdVisitor

class GdNewLineEndImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdNewLineEnd {
    fun accept(visitor: GdVisitor) {
        visitor.visitNewLineEnd(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
