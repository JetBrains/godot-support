package gdscript.psi

import com.intellij.psi.PsiElement

interface GdBindingPattern : PsiElement {
    val varNmi: GdVarNmi
}
