package gdscript.psi

interface GdPlusEx : GdExpr {
    val exprList: List<GdExpr>

    val sign: GdSign
}
