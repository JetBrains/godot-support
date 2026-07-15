package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdTopLevelDecl
import gdscript.psi.GdVisitor

open class GdTopLevelDeclImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdTopLevelDecl {
    open fun accept(visitor: GdVisitor) {
        visitor.visitTopLevelDecl(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }
}
