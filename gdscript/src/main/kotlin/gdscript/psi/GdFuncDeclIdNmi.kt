package gdscript.psi

import com.intellij.psi.PsiElement

interface GdFuncDeclIdNmi : GdNamedIdElement {
    override fun getName(): String

    override fun setName(newName: String): PsiElement

    override fun getNameIdentifier(): PsiElement
}
