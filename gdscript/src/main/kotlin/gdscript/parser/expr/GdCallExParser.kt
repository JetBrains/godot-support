package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdArgListParser
import gdscript.psi.GdTypes.*

object GdCallExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = CALL_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        var ok = true

        ok = ok && b.consumeToken(LRBR)
        ok = ok && GdArgListParser.parse(b, true)
        ok = ok && b.consumeToken(RRBR)

        return ok
    }

}
