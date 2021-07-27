package gdscript.psi.utils

import com.intellij.psi.PsiElement
import gdscript.psi.GdElementFactory
import gdscript.psi.GdTypes
import gdscript.psi.impl.GdConstIdNmiImpl

object PsiGdConstDeclUtil {

    fun setName(element: GdConstIdNmiImpl, newName: String?): PsiElement {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        if (keyNode != null) {
            val id = GdElementFactory.inheritanceName(element.project, newName!!);
            element.node.replaceChild(keyNode, id.node);
        }
        return element;
    }

    fun getName(element: GdConstIdNmiImpl): String {
        val valueNode = element.node.findChildByType(GdTypes.IDENTIFIER);
        return valueNode?.text ?: "";
    }

    fun getNameIdentifier(element: GdConstIdNmiImpl): PsiElement? {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER);
        return keyNode?.psi;
    }

}
