package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdKeyNmi
import gdscript.psi.GdPsiUtils.getName
import gdscript.psi.GdPsiUtils.getNameIdentifier
import gdscript.psi.GdPsiUtils.setName
import gdscript.psi.GdVisitor

class GdKeyNmiImpl(node: ASTNode) : GdNamedIdElementImpl(node), GdKeyNmi {
    fun accept(visitor: GdVisitor) {
        visitor.visitKeyNmi(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override fun getName(): String {
        // Strip the surrounding quotes of string-literal keys (e.g. {"key": value}) so the name matches the usage in dict.key
        val text = getName(this)
        if (text.length >= 2) {
            val first = text.get(0)
            if ((first == '"' || first == '\'') && text.get(text.length - 1) == first) {
                return text.substring(1, text.length - 1)
            }
        }
        return text
    }

    override fun setName(newName: String): PsiElement =
        setName(this, newName)

    override fun getNameIdentifier(): PsiElement =
        getNameIdentifier(this)
}
