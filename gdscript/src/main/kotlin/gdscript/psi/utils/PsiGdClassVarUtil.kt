package gdscript.psi.utils

import gdscript.psi.*

object PsiGdClassVarUtil {

    fun getReturnType(element: GdClassVarDeclTl): String {
        val stub = element.stub;
        if (stub !== null) {
            return stub.returnType();
        }

        if (element.typed !== null) {
            return PsiGdExprUtil.fromTyped(element.typed);
        }

        val asd = element.expr;
        val name = element.text;

        return element.expr?.returnType ?: "";
    }

}
