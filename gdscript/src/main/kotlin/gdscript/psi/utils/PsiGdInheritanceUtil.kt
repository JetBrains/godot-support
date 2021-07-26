package gdscript.psi.utils

import com.intellij.psi.PsiElement
import gdscript.psi.GdElementFactory.inheritanceName
import gdscript.psi.GdInheritanceIdNm
import gdscript.psi.GdTypes

object PsiGdInheritanceUtil {

    fun setName(element: GdInheritanceIdNm, newName: String?): PsiElement {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        if (keyNode != null) {
            val id = inheritanceName(element.project, newName!!)
            element.node.replaceChild(keyNode, id.node)
        }
        return element
    }

    fun getName(element: GdInheritanceIdNm): String {
        val valueNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        return valueNode?.text ?: ""
    }

    fun getNameIdentifier(element: GdInheritanceIdNm): PsiElement? {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        return keyNode?.psi
    }

}