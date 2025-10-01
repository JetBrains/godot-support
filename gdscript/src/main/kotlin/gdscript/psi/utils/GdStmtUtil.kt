package gdscript.psi.utils

import gdscript.psi.GdFlowSt
import gdscript.psi.GdForSt
import gdscript.psi.GdStmt
import gdscript.psi.GdWhileSt

object GdStmtUtil {

    fun getType(element: GdFlowSt): String {
        return element.firstChild.text
    }

    fun isLoopStatement(stmt: GdStmt): Boolean {
        return stmt is GdWhileSt || stmt is GdForSt
    }
}
