package gdscript.psi.utils

import gdscript.psi.*

object PsiGdClassVarUtil {

    fun getReturnType(element: GdClassVarDeclTl): String {
        if (element.typed !== null) {
            return PsiGdExprUtil.fromTyped(element.typed);
        }

        return element.expr?.returnType ?: "";
    }

}
