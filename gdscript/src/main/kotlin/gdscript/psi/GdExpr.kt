package gdscript.psi

import com.intellij.psi.PsiElement

interface GdExpr : PsiElement {
    val returnType: String

    fun getReturnTypeOrRes(allowResource: Boolean): String
}
