package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.NEGATE
import gdscript.psi.GdTypes.NEGATE_EX

object GdNegateExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = NEGATE_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        var ok = b.consumeToken(NEGATE, pin = true)
        ok = ok && GdExprParser.parse(b)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
