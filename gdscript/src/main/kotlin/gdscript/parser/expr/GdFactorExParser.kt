package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

// factor = sign { ( "*" | "/" | "%" ) sign }
object GdFactorExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = FACTOR_EX
    override val isNested = true

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "FactorExpr")) return false
        var ok = b.mceAnyOf(FACTOR_SIGN, false, MUL, DIV, MOD, POWER)
        b.pin(ok)
        ok = ok && GdExprParser.parseFrom(b, l, optional, POSITION + 1)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
