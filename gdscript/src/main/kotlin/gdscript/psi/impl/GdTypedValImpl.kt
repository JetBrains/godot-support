package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdPsiUtils.getReturnType
import gdscript.psi.GdTypeHint
import gdscript.psi.GdTypedVal
import gdscript.psi.GdVisitor

class GdTypedValImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdTypedVal {
    fun accept(visitor: GdVisitor) {
        visitor.visitTypedVal(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val typeHintList: List<GdTypeHint>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdTypeHint::class.java)

    override val returnType: String
        get() = getReturnType(this)
}
