package gdscript.psi

import com.intellij.psi.PsiElement

interface GdKeyValuePattern : PsiElement {
    val pattern: GdPattern?
}
