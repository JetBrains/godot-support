package gdscript.psi

interface GdMatchSt : GdStmt {
    val expr: GdExpr?

    val matchBlockList: List<GdMatchBlock>
}
