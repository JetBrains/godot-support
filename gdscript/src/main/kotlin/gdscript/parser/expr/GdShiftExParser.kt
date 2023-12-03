package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes
import gdscript.psi.GdTypes.LBSHIFT
import gdscript.psi.GdTypes.SHIFT_EX

object GdShiftExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = SHIFT_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        var ok = b.passToken(LBSHIFT, GdTypes.RBSHIFT)
        b.pin(ok)
        ok = ok && GdExprParser.parse(b)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
