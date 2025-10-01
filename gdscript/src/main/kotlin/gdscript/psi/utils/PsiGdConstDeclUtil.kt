package gdscript.psi.utils

import gdscript.psi.GdConstDeclTl

object PsiGdConstDeclUtil {

    fun getReturnType(element: GdConstDeclTl): String {
        if (element.typed !== null) {
            return PsiGdExprUtil.fromTyped(element.typed)
        }

        return element.expr?.returnType ?: ""
    }


}
