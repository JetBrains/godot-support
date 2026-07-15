package gdscript.psi

interface GdAttributeEx : GdExpr {
    val expr: GdExpr
    val refId: GdRefIdRef?
}
