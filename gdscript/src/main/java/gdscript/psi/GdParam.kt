package gdscript.psi

import com.intellij.psi.PsiElement

interface GdParam : PsiElement {
    val assignTyped: GdAssignTyped?

    val expr: GdExpr?

    val typed: GdTyped?

    val varNmi: GdVarNmi

    val returnType: String
}
