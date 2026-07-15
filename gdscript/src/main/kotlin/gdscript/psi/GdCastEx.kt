package gdscript.psi

interface GdCastEx : GdExpr {
    val expr: GdExpr

    val typedVal: GdTypedVal
}
