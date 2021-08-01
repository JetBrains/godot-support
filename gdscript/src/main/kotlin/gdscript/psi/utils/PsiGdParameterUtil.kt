package gdscript.psi.utils

import gdscript.psi.GdParam
import gdscript.psi.GdParamList

object PsiGdParameterUtil {

    fun fromString(data: String?): HashMap<String, String?> {
        val params = HashMap<String, String?>();
        if (data == null) {
            return params;
        }

        data.trim('{', '}').split(' ').forEach {
            val parts = it.split('=');
            if (parts.size == 2) {
                params[parts[0]] = if (parts[1] == "null") null else parts[1];
            }
        }

        return params;
    }

    fun toHashMap(paramList: GdParamList?): HashMap<String, String?> {
        val params = HashMap<String, String?>();
        var child = paramList?.firstChild;
        while (child != null) {
            if (child is GdParam) {
                val id = child.firstChild?.text;
                params[id.orEmpty()] = child.typed?.typeHintNm?.name;
            }
            child = child.nextSibling;
        }

        return params;
    }

}
