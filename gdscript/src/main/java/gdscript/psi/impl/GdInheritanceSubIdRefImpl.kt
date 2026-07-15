package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdInheritanceSubIdRef
import gdscript.psi.GdVisitor

class GdInheritanceSubIdRefImpl(node: ASTNode) : GdRefElementImpl(node), GdInheritanceSubIdRef {
    fun accept(visitor: GdVisitor) {
        visitor.visitInheritanceSubIdRef(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
