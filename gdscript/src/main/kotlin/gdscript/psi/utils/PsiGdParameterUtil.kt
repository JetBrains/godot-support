package gdscript.psi.utils

import gdscript.psi.GdParamList

object PsiGdParameterUtil {

    fun fromString(data: String?): LinkedHashMap<String, String?> {
        val params = LinkedHashMap<String, String?>()
        if (data == null) {
            return params
        }

        data.trim('{', '}').split(' ').forEach {
            val parts = it.split('=')
            if (parts.size == 2) {
                params[parts[0]] = if (parts[1] == "null") null else parts[1].trim(' ', ',')
            }
        }

        return params
    }

    fun toHashMap(paramList: GdParamList?): LinkedHashMap<String, String?> {
        val params = LinkedHashMap<String, String?>()

        paramList?.paramList?.forEach { param ->
            params[param.varNmi.text] = PsiGdExprUtil.fromTyped(param.typed)
        }

        return params
    }

}
