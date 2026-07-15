package gdscript.psi

import com.intellij.psi.PsiElement

interface GdTypeHint : PsiElement {
    val typeHintNmList: List<GdTypeHintRef>
}
