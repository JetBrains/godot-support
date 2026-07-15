package gdscript.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdAssignTyped
import gdscript.psi.GdExpr
import gdscript.psi.GdParam
import gdscript.psi.GdPsiUtils.getReturnType
import gdscript.psi.GdTyped
import gdscript.psi.GdVarNmi
import gdscript.psi.GdVisitor

class GdParamImpl(node: ASTNode) : ASTWrapperPsiElement(node), GdParam {
    fun accept(visitor: GdVisitor) {
        visitor.visitParam(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val assignTyped: GdAssignTyped?
        get() = PsiTreeUtil.getChildOfType(this, GdAssignTyped::class.java)

    override val expr: GdExpr?
        get() = PsiTreeUtil.getChildOfType(this, GdExpr::class.java)

    override val typed: GdTyped?
        get() = PsiTreeUtil.getChildOfType(this, GdTyped::class.java)

    override val varNmi: GdVarNmi
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdVarNmi::class.java))

    override val returnType: String
        get() = getReturnType(this)
}
