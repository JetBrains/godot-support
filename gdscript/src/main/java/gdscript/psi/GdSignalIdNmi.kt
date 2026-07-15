package gdscript.psi

import com.intellij.psi.PsiElement

interface GdSignalIdNmi : GdNamedIdElement {
    override fun getName(): String

    override fun setName(newName: String): PsiElement

    override fun getNameIdentifier(): PsiElement
}
