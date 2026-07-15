package gdscript.psi

import com.intellij.psi.PsiElement

interface GdSetgetDecl : PsiElement {
    val getDeclList: List<GdGetDecl>

    val setDeclList: List<GdSetDecl>
}
