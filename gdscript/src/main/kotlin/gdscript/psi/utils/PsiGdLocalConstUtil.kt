package gdscript.psi.utils

import gdscript.psi.*

object PsiGdLocalConstUtil {

    fun getReturnType(element: GdConstDeclSt): String {
        if (element.typed !== null) {
            return PsiGdExprUtil.fromTyped(element.typed);
        }

        return element.expr?.returnType ?: "";
    }

}
