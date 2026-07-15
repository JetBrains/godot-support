package gdscript.psi

import com.intellij.psi.PsiElement

interface GdReturnHintVal : PsiElement {
    val typedVal: GdTypedVal?
}
