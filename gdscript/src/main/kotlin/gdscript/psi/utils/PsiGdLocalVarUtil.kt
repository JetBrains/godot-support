package gdscript.psi.utils

import com.intellij.openapi.util.RecursionManager
import gdscript.psi.GdVarDeclSt

object PsiGdLocalVarUtil {

    fun getReturnType(element: GdVarDeclSt): String {
        if (element.typed !== null) {
            return PsiGdExprUtil.fromTyped(element.typed)
        }

        return RecursionManager.doPreventingRecursion(element, false) {
            element.expr?.returnType ?: ""
        } ?: ""
    }

}
