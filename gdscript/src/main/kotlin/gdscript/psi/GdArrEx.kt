package gdscript.psi

interface GdArrEx : GdExpr {
    val exprList: List<GdExpr>

    val baseExpr: GdExpr?
        get() = exprList.firstOrNull()

    val indexExpr: GdExpr?
        get() = exprList.getOrNull(1)

}
