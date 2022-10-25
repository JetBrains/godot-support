package gdscript.psi.utils

import com.intellij.openapi.util.Condition
import com.intellij.psi.PsiElement

object PsiGdTreeUtil {

    fun findFirstPrecedingElement(element: PsiElement, withSelf: Boolean = true, condition: Condition<in PsiElement?>): PsiElement? {
        var el: PsiElement? = element;
        if (!withSelf) {
            el = el?.prevSibling ?: el?.parent;
        }

        while (el != null) {
            if (condition.value(el)) {
                return el;
            }
            el = el.prevSibling ?: el.parent;
        }

        return null;
    }

}
