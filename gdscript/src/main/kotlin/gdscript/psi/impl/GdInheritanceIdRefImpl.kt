package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import gdscript.psi.GdInheritanceIdRef
import gdscript.psi.GdPsiUtils.getPsiFile
import gdscript.psi.GdPsiUtils.isClassName
import gdscript.psi.GdVisitor

class GdInheritanceIdRefImpl(node: ASTNode) : GdRefElementImpl(node), GdInheritanceIdRef {
    fun accept(visitor: GdVisitor) {
        visitor.visitInheritanceIdRef(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val psiFile: PsiFile?
        get() = getPsiFile(this)

    override val isClassName: Boolean
        get() = isClassName(this)
}
