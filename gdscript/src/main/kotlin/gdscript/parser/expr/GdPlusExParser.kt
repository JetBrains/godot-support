package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.PLUS
import gdscript.psi.GdTypes.PLUS_EX
import gdscript.psi.GdTypes.SIGN

// factor { "+" factor }
object GdPlusExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = PLUS_EX
    override val isNested = true

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "PlusExpr")) return false
        var ok = b.mceAnyOf(SIGN, false, PLUS)
        b.pin(ok)
        ok = ok && GdExprParser.parseFrom(b, l, optional, POSITION + 1)
        b.errorPin(ok, "expression")

        return ok
    }

}
