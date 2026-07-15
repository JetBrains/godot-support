package gdscript.psi

interface GdPrimaryEx : GdExpr {
    val arrayDecl: GdArrayDecl?

    val dictDecl: GdDictDecl?

    val expr: GdExpr?

    val nodePath: GdNodePath?
}
