package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdExpr
import gdscript.psi.GdPsiUtils.getReturnType
import gdscript.psi.GdPsiUtils.getReturnTypeOrRes
import gdscript.psi.GdVisitor

abstract class GdExprImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdExpr {
    open fun accept(visitor: GdVisitor) {
        visitor.visitExpr(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val returnType: String
        get() = getReturnType(this)

    override fun getReturnTypeOrRes(allowResource: Boolean): String =
        getReturnTypeOrRes(this, allowResource)
}
