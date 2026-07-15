package gdscript.psi

import com.intellij.psi.PsiElement

interface GdTypedVal : PsiElement {
    val typeHintList: List<GdTypeHint>

    val returnType: String
}
