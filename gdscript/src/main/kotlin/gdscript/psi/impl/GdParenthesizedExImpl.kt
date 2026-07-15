package gdscript.psi.impl

import com.intellij.lang.ASTNode
import gdscript.psi.GdExpr
import gdscript.psi.GdParenthesizedEx
import gdscript.psi.GdVisitor

class GdParenthesizedExImpl(node: ASTNode) : GdPrimaryExImpl(node), GdParenthesizedEx {
    override fun accept(visitor: GdVisitor) {
        visitor.visitParenthesizedEx(this)
    }

    override val containedExpression: GdExpr?
        get() = expr
}