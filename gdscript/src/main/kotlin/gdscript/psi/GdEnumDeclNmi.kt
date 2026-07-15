package gdscript.psi

import com.intellij.psi.PsiElement

interface GdEnumDeclNmi : GdNamedIdElement {
    override fun getName(): String

    override fun setName(newName: String): PsiElement

    override fun getNameIdentifier(): PsiElement
}
