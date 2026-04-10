package gdscript.psi.utils

import com.intellij.openapi.util.RecursionManager
import com.intellij.psi.PsiElement
import gdscript.psi.GdConstDeclSt

object PsiGdLocalConstUtil {

    fun getReturnType(element: GdConstDeclSt): String {
        if (element.typed !== null) {
            return PsiGdExprUtil.fromTyped(element.typed)
        }
        return RecursionManager.doPreventingRecursion(element, false) {
            element.expr?.returnType ?: ""
        } ?: ""
    }

    fun getReturnExpr(element: GdConstDeclSt): PsiElement? {
        return GdClassUtil.getClassIdElement(element.returnType, element)
    }

}
