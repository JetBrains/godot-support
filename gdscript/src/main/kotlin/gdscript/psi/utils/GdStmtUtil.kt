package gdscript.psi.utils

import gdscript.psi.*

object GdStmtUtil {

    fun getType(element: GdFlowSt): String {
        return element.firstChild.text;
    }

    fun isNestedStatement(stmt: GdStmt): Boolean {
        return stmt is GdIfSt || stmt is GdElifSt || stmt is GdElseSt || stmt is GdWhileSt || stmt is GdForSt || stmt is GdMatchSt
    }
}
