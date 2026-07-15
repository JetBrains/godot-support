package gdscript.psi

import com.intellij.psi.PsiElement

interface GdArgExpr : PsiElement {
    val expr: GdExpr

    val returnType: String
}
