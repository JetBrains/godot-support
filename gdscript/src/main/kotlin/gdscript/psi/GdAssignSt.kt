package gdscript.psi

interface GdAssignSt : GdStmt {
    val assignSign: GdAssignSign

    val endStmt: GdEndStmt

    val exprList: List<GdExpr>
}
