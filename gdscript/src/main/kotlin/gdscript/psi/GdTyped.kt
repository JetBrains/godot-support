package gdscript.psi

import com.intellij.psi.PsiElement

interface GdTyped : PsiElement {
    val typedVal: GdTypedVal
}
