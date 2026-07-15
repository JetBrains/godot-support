package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdReturnHint
import gdscript.psi.GdReturnHintVal
import gdscript.psi.GdVisitor

class GdReturnHintImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdReturnHint {
    fun accept(visitor: GdVisitor) {
        visitor.visitReturnHint(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val returnHintVal: GdReturnHintVal
        get() = notNullChild(
            PsiTreeUtil.getChildOfType(
                this,
                GdReturnHintVal::class.java
            )
        )
}
