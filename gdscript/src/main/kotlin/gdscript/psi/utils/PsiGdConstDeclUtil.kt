package gdscript.psi.utils

import gdscript.psi.GdConstDeclTl

object PsiGdConstDeclUtil {

    fun getReturnType(element: GdConstDeclTl): String {
        val stub = element.stub;
        if (stub !== null) {
            return stub.returnType();
        }

        if (element.typed !== null) {
            return PsiGdExprUtil.fromTyped(element.typed);
        }

        return element.expr?.returnType ?: "";
    }

}
