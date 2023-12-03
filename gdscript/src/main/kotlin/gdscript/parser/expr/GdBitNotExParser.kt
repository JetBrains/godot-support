package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.BIT_NOT_EX
import gdscript.psi.GdTypes.NOT

object GdBitNotExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = BIT_NOT_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        var ok = b.consumeToken(NOT, pin = true)
        ok = ok && GdExprParser.parse(b)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
