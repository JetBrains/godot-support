package gdscript.psi

import com.intellij.psi.PsiElement

interface GdReturnHint : PsiElement {
    val returnHintVal: GdReturnHintVal
}
