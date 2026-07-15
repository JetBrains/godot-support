package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdReturnHintVal
import gdscript.psi.GdTypedVal
import gdscript.psi.GdVisitor

class GdReturnHintValImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdReturnHintVal {
    fun accept(visitor: GdVisitor) {
        visitor.visitReturnHintVal(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val typedVal: GdTypedVal?
        get() = PsiTreeUtil.getChildOfType(this, GdTypedVal::class.java)
}
