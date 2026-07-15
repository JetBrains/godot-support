package gdscript.psi

interface GdExprSt : GdStmt {
    val endStmt: GdEndStmt

    val expr: GdExpr
}
