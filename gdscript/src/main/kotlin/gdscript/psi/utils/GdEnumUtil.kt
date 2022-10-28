package gdscript.psi.utils

import gdscript.psi.GdEnumDeclTl

object GdEnumUtil {

    fun getName(element: GdEnumDeclTl): String {
        val stub = element.stub;
        if (stub != null) return stub.name();
        return element.enumDeclNmi?.name.orEmpty();
    }

}
