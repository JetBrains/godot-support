package gdscript.psi.utils

import gdscript.psi.GdClassVarDeclTl

object GdClassVarUtil {

    fun getName(element: GdClassVarDeclTl): String {
        val stub = element.stub;
        if (stub !== null) {
            return stub.name();
        }

        return element.classVarIdNmi?.name.orEmpty();
    }

}
