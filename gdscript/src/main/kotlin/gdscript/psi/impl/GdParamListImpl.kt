package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdParam
import gdscript.psi.GdParamList
import gdscript.psi.GdVisitor

class GdParamListImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdParamList {
    fun accept(visitor: GdVisitor) {
        visitor.visitParamList(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val paramList: List<GdParam>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdParam::class.java)
}
