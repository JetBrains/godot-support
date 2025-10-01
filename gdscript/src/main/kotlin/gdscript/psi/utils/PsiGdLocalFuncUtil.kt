package gdscript.psi.utils

import com.intellij.psi.PsiElement
import gdscript.GdKeywords
import gdscript.psi.GdFuncDeclEx

object PsiGdLocalFuncUtil {

    fun getReturnType(element: GdFuncDeclEx): String {
        return element.returnHint?.returnHintVal?.text ?: ""
    }

    fun getReturnExpr(element: GdFuncDeclEx): PsiElement? {
        return GdClassUtil.getClassIdElement(GdKeywords.CALLABLE, element)
    }

    fun getParameters(element: GdFuncDeclEx): LinkedHashMap<String, String?> {
        return PsiGdParameterUtil.toHashMap(element.paramList)
    }

}
