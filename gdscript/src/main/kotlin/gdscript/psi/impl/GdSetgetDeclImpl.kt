package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdGetDecl
import gdscript.psi.GdSetDecl
import gdscript.psi.GdSetgetDecl
import gdscript.psi.GdVisitor

class GdSetgetDeclImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdSetgetDecl {
    fun accept(visitor: GdVisitor) {
        visitor.visitSetgetDecl(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val getDeclList: List<GdGetDecl>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdGetDecl::class.java)

    override val setDeclList: List<GdSetDecl>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdSetDecl::class.java)
}
