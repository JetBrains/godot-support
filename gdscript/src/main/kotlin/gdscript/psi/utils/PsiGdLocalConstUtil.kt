package gdscript.psi.utils

import gdscript.psi.*

object PsiGdLocalConstUtil {

    fun getName(element: GdConstDeclSt): String {
        return element.varNmi.name;
    }

    fun getReturnType(element: GdConstDeclSt): String {
        if (element.typed !== null) {
            return PsiGdExprUtil.fromTyped(element.typed);
        }

        return element.expr?.returnType ?: "";
    }

}
