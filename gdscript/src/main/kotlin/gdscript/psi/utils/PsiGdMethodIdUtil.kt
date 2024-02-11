package gdscript.psi.utils

import com.intellij.psi.util.findParentOfType
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi

object PsiGdMethodIdUtil {

    fun isConstructor(element: GdMethodIdNmi): Boolean {
        val methodParent = element.findParentOfType<GdMethodDeclTl>()
        return methodParent?.let { PsiGdMethodDeclUtil.isConstructor(methodParent)} ?: false
    }

}
