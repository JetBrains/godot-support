package gdscript.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.elementType
import gdscript.psi.GdTypes

object ElementTypeUtil {

    val SKIP_TOKEN = arrayOf(GdTypes.COMMENT, TokenType.WHITE_SPACE)

    fun hasChildOfType(element: PsiElement, elementType: IElementType): Boolean {
        var child = element.firstChild
        while (child != null) {
            if (child.elementType == elementType) return true
            child = child.nextSibling
        }

        return false
    }

    fun IElementType?.isSkipable(): Boolean {
        if (this == null) return false
        return SKIP_TOKEN.contains(this)
    }

}
