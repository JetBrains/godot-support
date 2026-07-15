package gdscript.psi

interface GdComparisonEx : GdExpr {
    val exprList: List<GdExpr>

    val operator: GdOperator
}
