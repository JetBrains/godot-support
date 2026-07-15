package gdscript.psi

interface GdWhileSt : GdStmt {
    val expr: GdExpr?

    val stmtOrSuite: GdStmtOrSuite?
}
