package gdscript.psi.utils

import com.intellij.psi.PsiElement
import gdscript.psi.GdElementFactory
import gdscript.psi.GdTypeHintNm
import gdscript.psi.GdTypes

object PsiGdTypeHintUtil {

    fun setName(element: GdTypeHintNm, newName: String?): PsiElement {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER);
        if (keyNode != null) {
            val id = GdElementFactory.identifier(element.project, newName!!)
            element.node.replaceChild(keyNode, id.node)
        } else {
            // TODO built-in type
        }

        return element
    }

    fun getName(element: GdTypeHintNm): String {
        return element.text ?: "";
    }

}
