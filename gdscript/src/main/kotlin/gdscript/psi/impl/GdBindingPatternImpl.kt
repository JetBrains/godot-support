package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdBindingPattern
import gdscript.psi.GdVarNmi
import gdscript.psi.GdVisitor

class GdBindingPatternImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdBindingPattern {
    fun accept(visitor: GdVisitor) {
        visitor.visitBindingPattern(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val varNmi: GdVarNmi
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdVarNmi::class.java))
}
