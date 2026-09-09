package gdscript.psi.utils

import gdscript.psi.GdMethodDeclTl

object GdMethodUtil {

    fun getName(element: GdMethodDeclTl): String {
        val stub = element.stub
        if (stub !== null) stub.name()

        return element.methodIdNmi?.name.orEmpty()
    }

}
