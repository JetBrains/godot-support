package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdGetMethodIdRef
import gdscript.psi.GdVisitor

class GdGetMethodIdRefImpl(node: ASTNode) : GdRefElementImpl(node), GdGetMethodIdRef {
    fun accept(visitor: GdVisitor) {
        visitor.visitGetMethodIdRef(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
