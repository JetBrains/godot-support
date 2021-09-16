package gdscript.psi.utils

import gdscript.psi.GdConstDeclTl

object PsiGdConstDeclUtil {

    fun getConstName(element: GdConstDeclTl): String? {
        val stub = element.stub;
        if (stub !== null) {
            return stub.name();
        }

        return element.constIdNmi?.name;
    }

    fun getReturnType(element: GdConstDeclTl): String {
        val stub = element.stub;
        if (stub !== null) {
            return stub.returnType();
        }

        return PsiGdExprUtil.fromTyped(element.typed);
    }

}
