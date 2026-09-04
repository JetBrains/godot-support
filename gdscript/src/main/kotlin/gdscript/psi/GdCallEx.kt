package gdscript.psi

interface GdCallEx : GdExpr {
    val argList: GdArgList?

    val expr: GdExpr
}
