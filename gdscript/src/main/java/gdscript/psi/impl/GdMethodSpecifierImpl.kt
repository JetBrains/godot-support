package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdMethodSpecifier
import gdscript.psi.GdVisitor

class GdMethodSpecifierImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdMethodSpecifier {
    fun accept(visitor: GdVisitor) {
        visitor.visitMethodSpecifier(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
