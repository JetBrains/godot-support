package gdscript.psi

interface GdForSt : GdStmt {
    val expr: GdExpr?

    val stmtOrSuite: GdStmtOrSuite?

    val varNmi: GdVarNmi?

    val typed: GdTyped?
}
