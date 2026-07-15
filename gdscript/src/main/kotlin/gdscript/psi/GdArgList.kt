package gdscript.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet

interface GdArgList : PsiElement {
    val argExprList: List<GdArgExpr>

    val closingParen: ASTNode?
        get() {
            val node = getNode()
            val children = node.getChildren(TokenSet.create(GdTypes.RRBR))
            return if (children.size == 0) null else children[children.size - 1]
        }
}
