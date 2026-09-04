package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdArrayDecl
import gdscript.psi.GdDictDecl
import gdscript.psi.GdExpr
import gdscript.psi.GdNodePath
import gdscript.psi.GdPrimaryEx
import gdscript.psi.GdVisitor

open class GdPrimaryExImpl(node: ASTNode) : GdExprImpl(node), GdPrimaryEx {
    override fun accept(visitor: GdVisitor) {
        visitor.visitPrimaryEx(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override val arrayDecl: GdArrayDecl?
        get() = PsiTreeUtil.getChildOfType(this, GdArrayDecl::class.java)

    override val dictDecl: GdDictDecl?
        get() = PsiTreeUtil.getChildOfType(this, GdDictDecl::class.java)

    override val expr: GdExpr?
        get() = PsiTreeUtil.getChildOfType(this, GdExpr::class.java)

    override val nodePath: GdNodePath?
        get() = PsiTreeUtil.getChildOfType(this, GdNodePath::class.java)
}
