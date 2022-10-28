package gdscript.psi.utils

import gdscript.psi.GdConstDeclTl

object GdConstDeclUtil {

    fun getName(element: GdConstDeclTl): String {
        val stub = element.stub;
        if (stub !== null) {
            return stub.name();
        }

        return element.constIdNmi?.name.orEmpty();
    }

}
