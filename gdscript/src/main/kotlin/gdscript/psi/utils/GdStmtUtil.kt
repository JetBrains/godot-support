package gdscript.psi.utils

import gdscript.psi.GdFlowSt

object GdStmtUtil {

    fun getType(element: GdFlowSt): String {
        return element.firstChild.text;
    }

}
