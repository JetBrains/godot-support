package gdscript.psi.utils

import gdscript.psi.GdTypedVal

object GdTypedUtil {

    fun getReturnType(element: GdTypedVal): String {
        return element.typeHintList.firstOrNull()?.text ?: ""
    }

}
