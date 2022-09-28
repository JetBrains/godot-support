package gdscript.psi.utils

import gdscript.psi.*

object PsiGdLocalVarUtil {

    fun getName(element: GdVarDeclSt): String {
        return element.varNmi.name;
    }

    fun getReturnType(element: GdVarDeclSt): String {
        if (element.typed !== null) {
            return PsiGdExprUtil.fromTyped(element.typed);
        }

        return element.expr?.returnType ?: "";
    }

}
