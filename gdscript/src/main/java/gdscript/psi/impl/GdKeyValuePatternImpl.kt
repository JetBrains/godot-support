package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdKeyValuePattern
import gdscript.psi.GdPattern
import gdscript.psi.GdVisitor

class GdKeyValuePatternImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdKeyValuePattern {
    fun accept(visitor: GdVisitor) {
        visitor.visitKeyValuePattern(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val pattern: GdPattern?
        get() = PsiTreeUtil.getChildOfType(this, GdPattern::class.java)
}
