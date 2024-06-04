package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdTypedParser
import gdscript.psi.GdTypes.AS
import gdscript.psi.GdTypes.CAST_EX

// ternaryExpr [ "as" typeHint ];
object GdCastExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = CAST_EX
    override val isNested = true

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "CastExpr")) return false
        var ok = b.consumeToken(AS, pin = true)
        ok = ok && GdTypedParser.typedVal(b, l + 1, false)

        return ok
    }

}
