package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdFactorSign
import gdscript.psi.GdVisitor

class GdFactorSignImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdFactorSign {
    fun accept(visitor: GdVisitor) {
        visitor.visitFactorSign(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
