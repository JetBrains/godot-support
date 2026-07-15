package gdscript.psi

import com.intellij.psi.PsiElement

interface GdArrayDecl : PsiElement {
    val exprList: List<GdExpr>
}
