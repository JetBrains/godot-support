package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.model.GdTutorial
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdFuncDeclIdNmi
import gdscript.psi.GdParamList
import gdscript.psi.GdPsiUtils.getInvokedReturnType
import gdscript.psi.GdPsiUtils.getParameters
import gdscript.psi.GdPsiUtils.getReturnExpr
import gdscript.psi.GdReturnHint
import gdscript.psi.GdStmtOrSuite
import gdscript.psi.GdVisitor
import gdscript.psi.utils.GdCommentUtil.brief
import gdscript.psi.utils.GdCommentUtil.description
import gdscript.psi.utils.GdCommentUtil.isDeprecated
import gdscript.psi.utils.GdCommentUtil.isExperimental
import gdscript.psi.utils.GdCommentUtil.tutorials

class GdFuncDeclExImpl(node: ASTNode) : GdExprImpl(node), GdFuncDeclEx {
    override fun accept(visitor: GdVisitor) {
        visitor.visitFuncDeclEx(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val funcDeclIdNmi: GdFuncDeclIdNmi?
        get() = PsiTreeUtil.getChildOfType(this, GdFuncDeclIdNmi::class.java)

    override val paramList: GdParamList?
        get() = PsiTreeUtil.getChildOfType(this, GdParamList::class.java)

    override val returnHint: GdReturnHint?
        get() = PsiTreeUtil.getChildOfType(this, GdReturnHint::class.java)

    override val stmtOrSuite: GdStmtOrSuite?
        get() = PsiTreeUtil.getChildOfType(this, GdStmtOrSuite::class.java)

    override val returnType: String
        get() = GdKeywords.CALLABLE

    override val invokedReturnType: String
        get() = getInvokedReturnType(this)

    override val returnExpr: PsiElement?
        get() = getReturnExpr(this)

    override val parameters: LinkedHashMap<String, String>
        get() = getParameters(this)

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
