package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

object GdArrayExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = ARR_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        var ok = b.consumeToken(LSBR, pin = true)
        ok = ok && GdExprParser.parse(b, true)
        ok = ok && b.consumeToken(RSBR)

        return ok
    }

}
