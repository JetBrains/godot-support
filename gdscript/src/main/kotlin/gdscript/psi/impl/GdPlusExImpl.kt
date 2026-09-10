package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdExpr
import gdscript.psi.GdPlusEx
import gdscript.psi.GdSign
import gdscript.psi.GdVisitor

class GdPlusExImpl(node: ASTNode) : GdExprImpl(node), GdPlusEx {
    override fun accept(visitor: GdVisitor) {
        visitor.visitPlusEx(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val exprList: List<GdExpr>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdExpr::class.java)

    override val sign: GdSign
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdSign::class.java))
}
