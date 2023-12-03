package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

object GdTernaryExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = TERNARY_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        var ok = b.consumeToken(IF, pin = true)
        ok = ok && GdExprParser.parse(b)
        b.errorPin(ok, "expression")
        ok = ok && b.consumeToken(ELSE)
        ok = ok && GdExprParser.parse(b)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
