package gdscript.psi

import com.intellij.psi.PsiElement

interface GdPattern : PsiElement {
    val arrayPattern: GdArrayPattern?

    val bindingPattern: GdBindingPattern?

    val dictPattern: GdDictPattern?

    val expr: GdExpr?
}
