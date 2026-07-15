package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.model.GdTutorial
import gdscript.psi.GdAssignTyped
import gdscript.psi.GdEndStmt
import gdscript.psi.GdExpr
import gdscript.psi.GdPsiUtils.getName
import gdscript.psi.GdPsiUtils.getReturnType
import gdscript.psi.GdTyped
import gdscript.psi.GdVarDeclSt
import gdscript.psi.GdVarNmi
import gdscript.psi.GdVisitor
import gdscript.psi.utils.GdCommentUtil.brief
import gdscript.psi.utils.GdCommentUtil.description
import gdscript.psi.utils.GdCommentUtil.isDeprecated
import gdscript.psi.utils.GdCommentUtil.isExperimental
import gdscript.psi.utils.GdCommentUtil.tutorials

class GdVarDeclStImpl(node: ASTNode) : GdStmtImpl(node), GdVarDeclSt {
    override fun accept(visitor: GdVisitor) {
        visitor.visitVarDeclSt(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val assignTyped: GdAssignTyped?
        get() = PsiTreeUtil.getChildOfType(this, GdAssignTyped::class.java)

    override val endStmt: GdEndStmt?
        get() = PsiTreeUtil.getChildOfType(this, GdEndStmt::class.java)

    override val expr: GdExpr?
        get() = PsiTreeUtil.getChildOfType(this, GdExpr::class.java)

    override val typed: GdTyped?
        get() = PsiTreeUtil.getChildOfType(this, GdTyped::class.java)

    override val varNmi: GdVarNmi?
        get() = PsiTreeUtil.getChildOfType(this, GdVarNmi::class.java)

    override fun getName(): String =
        getName(this)

    override val returnType: String
        get() = getReturnType(this)

    override fun description(): String =
        description(this)

    override fun brief(): String =
        brief(this)

    override fun tutorials(): List<GdTutorial> =
        tutorials(this)

    override fun isDeprecated(): Boolean =
        isDeprecated(this)

    override fun isExperimental(): Boolean =
        isExperimental(this)
}
