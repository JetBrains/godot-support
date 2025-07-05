package gdscript.psi.utils

import gdscript.psi.*

object PsiGdLocalVarUtil {

    fun getReturnType(element: GdVarDeclSt): String {
        if (element.typed !== null) {
            return PsiGdExprUtil.fromTyped(element.typed);
        }

        return element.expr?.returnType ?: "";
    }

}
