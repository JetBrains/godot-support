package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

object GdBitExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = BIT_AND_EX

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "BitExpr")) return false
        var ok = b.mceAnyOf(BIT_AND_SIGN, false, AND, XOR, OR)
        b.pin(ok)
        ok = ok && GdExprParser.parse(b, l + 1)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
