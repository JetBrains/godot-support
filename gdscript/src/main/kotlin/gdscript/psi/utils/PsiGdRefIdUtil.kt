package gdscript.psi.utils

import com.intellij.psi.PsiElement
import gdscript.psi.GdElementFactory
import gdscript.psi.GdRefIdNm
import gdscript.psi.GdTypes

object PsiGdRefIdUtil {

    fun setName(element: GdRefIdNm, newName: String?): PsiElement {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER);
        if (keyNode != null) {
            val id = GdElementFactory.identifier(element.project, newName!!)
            element.node.replaceChild(keyNode, id.node)
        }

        return element
    }

    fun getName(element: GdRefIdNm): String {
        return element.text ?: "";
    }

}
