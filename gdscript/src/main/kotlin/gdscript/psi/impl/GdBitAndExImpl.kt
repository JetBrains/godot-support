package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdBitAndEx
import gdscript.psi.GdBitAndSign
import gdscript.psi.GdExpr
import gdscript.psi.GdVisitor

class GdBitAndExImpl(node: ASTNode) : GdExprImpl(node), GdBitAndEx {
    override fun accept(visitor: GdVisitor) {
        visitor.visitBitAndEx(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val bitAndSign: GdBitAndSign
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdBitAndSign::class.java))

    override val exprList: List<GdExpr>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdExpr::class.java)
}
