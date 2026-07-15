package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdPattern
import gdscript.psi.GdPatternList
import gdscript.psi.GdVisitor

class GdPatternListImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdPatternList {
    fun accept(visitor: GdVisitor) {
        visitor.visitPatternList(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val patternList: List<GdPattern>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdPattern::class.java)
}
