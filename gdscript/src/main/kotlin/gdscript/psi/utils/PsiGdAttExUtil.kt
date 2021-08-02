package gdscript.psi.utils

import com.intellij.psi.PsiElement
import gdscript.psi.GdAttExNm
import gdscript.psi.GdElementFactory
import gdscript.psi.GdTypes

object PsiGdAttExUtil {

    fun setName(element: GdAttExNm, newName: String?): PsiElement {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER);
        if (keyNode != null) {
            val id = GdElementFactory.identifier(element.project, newName!!)
            element.node.replaceChild(keyNode, id.node)
        }

        return element
    }

    fun getName(element: GdAttExNm): String {
        return element.text ?: "";
    }

}
