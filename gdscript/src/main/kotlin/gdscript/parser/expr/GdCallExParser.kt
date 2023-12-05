package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdArgListParser
import gdscript.psi.GdTypes.*

object GdCallExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = CALL_EX

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "CallExpr")) return false
        var ok = true

        ok = ok && b.consumeToken(LRBR)
        ok = ok && GdArgListParser.parse(b, l + 1, true)
        ok = ok && b.consumeToken(RRBR)

        return ok
    }

}
