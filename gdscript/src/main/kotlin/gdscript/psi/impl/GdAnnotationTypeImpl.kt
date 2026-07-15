package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdAnnotationType
import gdscript.psi.GdVisitor

class GdAnnotationTypeImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdAnnotationType {
    fun accept(visitor: GdVisitor) {
        visitor.visitAnnotationType(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
