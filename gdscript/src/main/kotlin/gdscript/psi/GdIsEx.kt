package gdscript.psi

interface GdIsEx : GdExpr {
    val expr: GdExpr

    val typedVal: GdTypedVal
}
