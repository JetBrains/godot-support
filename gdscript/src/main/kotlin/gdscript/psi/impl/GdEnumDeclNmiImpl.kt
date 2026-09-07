package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdEnumDeclNmi
import gdscript.psi.GdPsiUtils.getName
import gdscript.psi.GdPsiUtils.getNameIdentifier
import gdscript.psi.GdPsiUtils.setName
import gdscript.psi.GdVisitor

class GdEnumDeclNmiImpl(node: ASTNode) : GdNamedIdElementImpl(node), GdEnumDeclNmi {
    fun accept(visitor: GdVisitor) {
        visitor.visitEnumDeclNmi(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override fun getName(): String =
        getName(this)

    override fun setName(newName: String): PsiElement =
        setName(this, newName)

    override fun getNameIdentifier(): PsiElement =
        getNameIdentifier(this)
}
