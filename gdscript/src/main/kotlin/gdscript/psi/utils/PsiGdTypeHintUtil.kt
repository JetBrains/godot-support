package gdscript.psi.utils

import com.intellij.psi.PsiElement
import gdscript.psi.GdElementFactory
import gdscript.psi.GdTypeHintNm
import gdscript.psi.GdTypes

object PsiGdTypeHintUtil {

    fun setName(element: GdTypeHintNm, newName: String?): PsiElement {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        if (keyNode != null) {
            val id = GdElementFactory.inheritanceName(element.project, newName!!)
            element.node.replaceChild(keyNode, id.node)
        }
        return element
    }

    fun getName(element: GdTypeHintNm): String {
        val valueNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        return valueNode?.text ?: ""
    }

}
