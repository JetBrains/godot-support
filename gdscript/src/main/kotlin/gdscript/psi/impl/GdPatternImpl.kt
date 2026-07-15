package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdArrayPattern
import gdscript.psi.GdBindingPattern
import gdscript.psi.GdDictPattern
import gdscript.psi.GdExpr
import gdscript.psi.GdPattern
import gdscript.psi.GdVisitor

class GdPatternImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdPattern {
    fun accept(visitor: GdVisitor) {
        visitor.visitPattern(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val arrayPattern: GdArrayPattern?
        get() = PsiTreeUtil.getChildOfType(this, GdArrayPattern::class.java)

    override val bindingPattern: GdBindingPattern?
        get() = PsiTreeUtil.getChildOfType(this, GdBindingPattern::class.java)

    override val dictPattern: GdDictPattern?
        get() = PsiTreeUtil.getChildOfType(this, GdDictPattern::class.java)

    override val expr: GdExpr?
        get() = PsiTreeUtil.getChildOfType(this, GdExpr::class.java)
}
