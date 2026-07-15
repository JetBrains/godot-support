package gdscript.psi

import gdscript.psi.types.GdDocumented

interface GdVarDeclSt : GdStmt, GdDocumented {
    val assignTyped: GdAssignTyped?

    val endStmt: GdEndStmt?

    val expr: GdExpr?

    val typed: GdTyped?

    val varNmi: GdVarNmi?

    fun getName(): String

    val returnType: String
}
