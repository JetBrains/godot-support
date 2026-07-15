package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdTypeHint
import gdscript.psi.GdTypeHintRef
import gdscript.psi.GdVisitor

class GdTypeHintImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdTypeHint {
    fun accept(visitor: GdVisitor) {
        visitor.visitTypeHint(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val typeHintNmList: List<GdTypeHintRef>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdTypeHintRef::class.java)
}
