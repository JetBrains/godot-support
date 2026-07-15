package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdStringValRef
import gdscript.psi.GdVisitor

class GdStringValRefImpl(node: ASTNode) : GdRefElementImpl(node), GdStringValRef {
    fun accept(visitor: GdVisitor) {
        visitor.visitStringVal(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
