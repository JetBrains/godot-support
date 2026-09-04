package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdBitAndSign
import gdscript.psi.GdVisitor

class GdBitAndSignImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdBitAndSign {
    fun accept(visitor: GdVisitor) {
        visitor.visitBitAndSign(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
