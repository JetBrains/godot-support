package gdscript.psi.utils

import gdscript.psi.*
import gdscript.utils.ElementTypeUtil

object PsiGdMethodDeclUtil {

    fun isStatic(element: GdMethodDeclTl): Boolean {
        val stub = element.stub;
        if (stub !== null) return stub.isStatic();

        return ElementTypeUtil.hasChildOfType(element, GdTypes.STATIC);
    }

    fun isVariadic(element: GdMethodDeclTl): Boolean {
        val stub = element.stub;
        if (stub !== null) {
            return stub.isVariadic();
        }

        return ElementTypeUtil.hasChildOfType(element, GdTypes.VARARG);
    }

    fun getReturnType(element: GdMethodDeclTl): String {
        val stub = element.stub;
        if (stub !== null) {
            return stub.returnType();
        }

        return element.returnHint?.returnHintVal?.text ?: "";
    }

    fun getReturnType(element: GdParam): String {
        return element.typed?.typedVal?.returnType ?: "";
    }

    fun getParameters(element: GdMethodDeclTl): HashMap<String, String?> {
        val stub = element.stub;
        if (stub !== null) {
            return stub.parameters();
        }

        return PsiGdParameterUtil.toHashMap(element.paramList)
    }

    fun isConstructor(element: GdMethodDeclTl): Boolean {
        val stub = element.stub;
        if (stub != null) return stub.isConstructor();

        return element.name == "_init"
                || GdClassUtil.getOwningClassName(element) == element.name;
    }

}
