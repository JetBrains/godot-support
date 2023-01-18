package gdscript.psi.utils

import gdscript.psi.GdConstDeclSt
import gdscript.psi.GdConstDeclTl

object GdConstDeclUtil {

    fun getName(element: GdConstDeclTl): String {
        val stub = element.stub;
        if (stub !== null) {
            return stub.name();
        }

        return element.constIdNmi?.name.orEmpty();
    }

    /** Local */

    fun getName(element: GdConstDeclSt): String {
        return element.varNmi?.name ?: "";
    }

}
