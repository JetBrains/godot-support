package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdExpr
import gdscript.psi.GdFactorEx
import gdscript.psi.GdFactorSign
import gdscript.psi.GdVisitor

class GdFactorExImpl(node: ASTNode) : GdExprImpl(node), GdFactorEx {
    override fun accept(visitor: GdVisitor) {
        visitor.visitFactorEx(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val exprList: List<GdExpr>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdExpr::class.java)

    override val factorSign: GdFactorSign
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdFactorSign::class.java))
}
