package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

object GdComparisonExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = COMPARISON_EX

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "ComparisonExpr")) return false
        var ok = b.mceAnyOf(OPERATOR, false, TEST_OPERATOR)
        b.pin(ok)
        ok = ok && GdExprParser.parse(b, l + 1)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
