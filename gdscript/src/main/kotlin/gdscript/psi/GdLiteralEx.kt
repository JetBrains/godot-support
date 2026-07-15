package gdscript.psi

interface GdLiteralEx : GdExpr {
    val refIdNm: GdRefIdRef?

    val stringVal: GdStringValRef?
}
