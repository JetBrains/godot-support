package gdscript.psi.utils

import gdscript.psi.GdSignalDeclTl

object GdSignalUtil {

    fun getName(element: GdSignalDeclTl): String {
        val stub = element.stub;
        if (stub !== null) return stub.name();

        return element.signalIdNmi?.name ?: "";
    }

}