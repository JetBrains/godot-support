package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdVisitor

class GdRefIdRefImpl(node: ASTNode) : GdRefElementImpl(node), GdRefIdRef {
    fun accept(visitor: GdVisitor) {
        visitor.visitRefIdNm(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
