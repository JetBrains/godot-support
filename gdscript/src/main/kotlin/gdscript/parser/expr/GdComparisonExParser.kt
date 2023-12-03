package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

object GdComparisonExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = COMPARISON_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        var ok = b.mceAnyOf(OPERATOR, false, TEST_OPERATOR)
        b.pin(ok)
        ok = ok && GdExprParser.parse(b)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
