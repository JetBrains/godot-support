package gdscript.psi.utils

import com.intellij.psi.PsiElement
import gdscript.psi.*

object PsiGdVarNmiUtil {

    fun getName(element: GdVarNmi): String {
        val valueNode = element.node.findChildByType(GdTypes.IDENTIFIER)

        return valueNode?.text ?: ""
    }

    fun setName(element: GdVarNmi, newName: String?): PsiElement {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        if (keyNode != null) {
            val id = GdElementFactory.identifier(element.project, newName!!)
            element.node.replaceChild(keyNode, id.node)
        }
        return element
    }

    fun getNameIdentifier(element: GdVarNmi): PsiElement? {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER);

        return keyNode?.psi;
    }

}