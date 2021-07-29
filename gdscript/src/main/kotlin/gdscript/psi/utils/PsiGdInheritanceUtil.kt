package gdscript.psi.utils

import com.intellij.psi.PsiElement
import gdscript.psi.GdElementFactory.identifier
import gdscript.psi.GdInheritanceIdNmi
import gdscript.psi.GdTypes

object PsiGdInheritanceUtil {

    fun setName(element: GdInheritanceIdNmi, newName: String?): PsiElement {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        if (keyNode != null) {
            val id = identifier(element.project, newName!!)
            element.node.replaceChild(keyNode, id.node)
        }
        return element
    }

    fun getName(element: GdInheritanceIdNmi): String {
        val valueNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        return valueNode?.text ?: ""
    }

    fun getNameIdentifier(element: GdInheritanceIdNmi): PsiElement? {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        return keyNode?.psi
    }

}