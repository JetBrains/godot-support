package gdscript.psi.utils

import gdscript.psi.GdVarDeclSt

object GdVarDeclStUtil {

    fun getName(element: GdVarDeclSt): String {
        return element.varNmi.name;
    }

}
