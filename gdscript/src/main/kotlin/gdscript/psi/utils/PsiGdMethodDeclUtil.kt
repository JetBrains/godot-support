package gdscript.psi.utils

import gdscript.GdKeywords
import gdscript.psi.*

object PsiGdMethodDeclUtil {

    fun isStatic(element: GdMethodDeclTl): Boolean {
        val stub = element.stub
        if (stub !== null) return stub.isStatic()

        return element.methodSpecifierList.any { it.text == GdKeywords.STATIC }
    }

    fun isVariadic(element: GdMethodDeclTl): Boolean {
        val stub = element.stub
        if (stub !== null) {
            return stub.isVariadic()
        }

        return element.methodSpecifierList.any { it.text == GdKeywords.VARARG }
    }

    fun getReturnType(element: GdMethodDeclTl): String {
        val stub = element.stub
        if (stub !== null) {
            return stub.returnType()
        }

        return element.returnHint?.returnHintVal?.text ?: ""
    }

    fun getReturnType(element: GdParam): String {
        element.typed?.let { return PsiGdExprUtil.fromTyped(it) }

        return element.expr?.returnType ?: ""
    }

    fun getParameters(element: GdMethodDeclTl): LinkedHashMap<String, String?> {
        val stub = element.stub
        if (stub !== null) {
            return stub.parameters()
        }

        return PsiGdParameterUtil.toHashMap(element.paramList)
    }

    fun isConstructor(element: GdMethodDeclTl): Boolean {
        val stub = element.stub
        if (stub != null) return stub.isConstructor()

        return element.name == "_init"
                || GdClassUtil.getOwningClassName(element) == element.name
    }

}
