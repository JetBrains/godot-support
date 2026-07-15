package gdscript.psi

interface GdIfSt : GdStmt {
    val elifStList: List<GdElifSt>

    val elseSt: GdElseSt?

    val expr: GdExpr?

    val stmtOrSuite: GdStmtOrSuite?
}
