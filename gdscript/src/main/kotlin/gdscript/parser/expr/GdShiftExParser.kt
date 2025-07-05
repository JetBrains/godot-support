package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes
import gdscript.psi.GdTypes.LBSHIFT
import gdscript.psi.GdTypes.SHIFT_EX

// minus { ( "<<" | ">>" ) minus }
object GdShiftExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = SHIFT_EX
    override val isNested = true

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "ShiftExpr")) return false
        var ok = b.passToken(LBSHIFT, GdTypes.RBSHIFT)
        b.pin(ok)
        ok = ok && GdExprParser.parseFrom(b, l, optional, POSITION + 1)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
