package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

object GdArrayExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = ARR_EX

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "ArrayExpr")) return false
        var ok = b.consumeToken(LSBR, pin = true)
        ok = ok && GdExprParser.parse(b, l + 1, true)
        ok = ok && b.consumeToken(RSBR)

        return ok || b.pinned()
    }

}
