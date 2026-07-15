package gdscript.psi

interface GdBitAndEx : GdExpr {
    val bitAndSign: GdBitAndSign

    val exprList: List<GdExpr>
}
