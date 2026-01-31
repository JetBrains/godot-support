package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.BIT_AND_EX
import gdscript.psi.GdTypes.BIT_AND_SIGN
import gdscript.psi.GdTypes.XOR

// bitAnd { "^" bitAnd }
object GdBitXorExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = BIT_AND_EX
    override val isNested = true

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "BitXorExpr")) return false
        var ok = b.mceAnyOf(BIT_AND_SIGN, false, XOR)
        b.pin(ok)
        ok = ok && GdExprParser.parseFrom(b, l, optional, POSITION + 1)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
