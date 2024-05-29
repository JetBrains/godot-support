package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

// plus { "-" plus }
object GdMinusExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = PLUS_EX

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "MinusExpr")) return false
        var ok = b.mceAnyOf(SIGN, false, MINUS)
        b.pin(ok)
        ok = ok && GdExprParser.parseFrom(b, l, optional, POSITION + 1)
        b.errorPin(ok, "expression")

        return ok
    }

}
