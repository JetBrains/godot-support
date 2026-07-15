package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdTypeHintRef
import gdscript.psi.GdVisitor

class GdTypeHintRefImpl(node: ASTNode) : GdRefElementImpl(node), GdTypeHintRef {
    fun accept(visitor: GdVisitor) {
        visitor.visitTypeHintRef(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
