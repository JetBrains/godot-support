package gdscript.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.elementType

object ElementTypeUtil {

    fun hasChildOfType(element: PsiElement, elementType: IElementType): Boolean {
        var child = element.firstChild;
        while (child != null) {
            if (child.elementType == elementType) return true;
            child = child.nextSibling;
        }

        return false;
    }

}
