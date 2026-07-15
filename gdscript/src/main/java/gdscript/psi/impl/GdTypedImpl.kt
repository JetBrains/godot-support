package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdTyped
import gdscript.psi.GdTypedVal
import gdscript.psi.GdVisitor

class GdTypedImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdTyped {
    fun accept(visitor: GdVisitor) {
        visitor.visitTyped(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val typedVal: GdTypedVal
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdTypedVal::class.java))
}
