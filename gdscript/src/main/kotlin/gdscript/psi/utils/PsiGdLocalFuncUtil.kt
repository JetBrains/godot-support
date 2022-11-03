package gdscript.psi.utils

import gdscript.psi.GdFuncDeclEx

object PsiGdLocalFuncUtil {

    fun getReturnType(element: GdFuncDeclEx): String {
        return element.returnHint?.returnHintVal?.text ?: "";
    }

    fun getParameters(element: GdFuncDeclEx): HashMap<String, String?> {
        return PsiGdParameterUtil.toHashMap(element.paramList);
    }

}
