package gdscript.psi.utils

import gdscript.psi.*

object PsiGdClassVarUtil {

    fun getName(element: GdClassVarDeclTl): String? {
        val stub = element.stub;
        if (stub !== null) {
            return stub.name();
        }

        return element.classVarIdNmi?.name;
    }

    fun getReturnType(element: GdClassVarDeclTl): String {
        val stub = element.stub;
        if (stub !== null) {
            return stub.returnType();
        }

        return element.typed?.typeHintNm?.name ?: "";
    }

}
