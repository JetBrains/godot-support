package gdscript.psi

interface GdFactorEx : GdExpr {
    val exprList: List<GdExpr>

    val factorSign: GdFactorSign
}
