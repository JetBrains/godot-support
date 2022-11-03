package gdscript.psi.utils

import gdscript.psi.GdTypeHintNm

object PsiGdTypeHintUtil {

    fun getName(element: GdTypeHintNm): String {
        return element.text ?: "";
    }

}
