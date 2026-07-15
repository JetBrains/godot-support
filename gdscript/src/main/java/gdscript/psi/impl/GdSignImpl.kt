package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdSign
import gdscript.psi.GdVisitor

class GdSignImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdSign {
    fun accept(visitor: GdVisitor) {
        visitor.visitSign(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
