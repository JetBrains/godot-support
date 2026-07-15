package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdExpr
import gdscript.psi.GdMatchBlock
import gdscript.psi.GdMatchSt
import gdscript.psi.GdVisitor

class GdMatchStImpl(node: ASTNode) : GdStmtImpl(node), GdMatchSt {
    override fun accept(visitor: GdVisitor) {
        visitor.visitMatchSt(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val expr: GdExpr?
        get() = PsiTreeUtil.getChildOfType(this, GdExpr::class.java)

    override val matchBlockList: List<GdMatchBlock>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, GdMatchBlock::class.java)
}
