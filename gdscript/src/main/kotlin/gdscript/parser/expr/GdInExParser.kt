package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.IN
import gdscript.psi.GdTypes.IN_EX

object GdInExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = IN_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        var ok = b.consumeToken(IN, pin = true)
        ok = ok && GdExprParser.parse(b)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
