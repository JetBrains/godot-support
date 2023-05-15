package gdscript.psi.utils

import gdscript.psi.GdClassVarDeclTl

/**
 * GdClassVarDeclTl psi utils
 */
object GdClassVarUtil {

    fun getName(element: GdClassVarDeclTl): String {
        val stub = element.stub
        if (stub !== null) return stub.name()

        return element.varNmi?.name.orEmpty()
    }

}
