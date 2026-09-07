package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdCastEx
import gdscript.psi.GdExpr
import gdscript.psi.GdTypedVal
import gdscript.psi.GdVisitor

class GdCastExImpl(node: ASTNode) : GdExprImpl(node), GdCastEx {
    override fun accept(visitor: GdVisitor) {
        visitor.visitCastEx(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val expr: GdExpr
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdExpr::class.java))

    override val typedVal: GdTypedVal
        get() = notNullChild(PsiTreeUtil.getChildOfType(this, GdTypedVal::class.java))
}
