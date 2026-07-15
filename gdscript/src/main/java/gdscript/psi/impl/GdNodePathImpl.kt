package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdNodePath
import gdscript.psi.GdVisitor

class GdNodePathImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdNodePath {
    fun accept(visitor: GdVisitor) {
        visitor.visitNodePath(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
