package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdAnnotationParams
import gdscript.psi.GdExpr
import gdscript.psi.GdVisitor

class GdAnnotationParamsImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdAnnotationParams {
    fun accept(visitor: GdVisitor) {
        visitor.visitAnnotationParams(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val exprList: List<GdExpr>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdExpr::class.java)
}
