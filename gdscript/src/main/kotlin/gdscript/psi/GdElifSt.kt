package gdscript.psi

interface GdElifSt : GdStmt {
    val expr: GdExpr?

    val stmtOrSuite: GdStmtOrSuite?
}
