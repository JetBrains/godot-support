package gdscript.psi

interface GdFlowSt : GdStmt {
    val endStmt: GdEndStmt

    val expr: GdExpr?

    val type: String
}
