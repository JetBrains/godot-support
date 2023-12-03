package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

object GdLogicExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = LOGIC_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        var ok = b.passToken(ANDAND, OROR)
        b.pin(ok)
        ok = ok && GdExprParser.parse(b)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
