package gdscript.psi.utils

import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdVarDeclSt
import gdscript.psi.GdVarNmi

object PsiGdLocalFuncUtil {

    fun getReturnType(element: GdFuncDeclEx): String {
        return element.returnHint?.returnHintVal?.text ?: "";
    }

    fun getParameters(element: GdFuncDeclEx): HashMap<String, String?> {
        return PsiGdParameterUtil.toHashMap(element.paramList);
    }

    fun getByVarId(element: GdVarNmi): GdFuncDeclEx? {
        val variable = PsiTreeUtil.getParentOfType(element, GdVarDeclSt::class.java) ?: return null;
        if (variable.expr is GdFuncDeclEx) {
            return variable.expr as GdFuncDeclEx;
        }

        return null;
    }

}
