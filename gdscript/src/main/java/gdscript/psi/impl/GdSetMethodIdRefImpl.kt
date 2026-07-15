package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdSetMethodIdRef
import gdscript.psi.GdVisitor

class GdSetMethodIdRefImpl(node: ASTNode) : GdRefElementImpl(node), GdSetMethodIdRef {
    fun accept(visitor: GdVisitor) {
        visitor.visitSetMethodIdRef(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
