package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.NEGATE
import gdscript.psi.GdTypes.NEGATE_EX

// ( "!" | "not" ) logicNot | in
object GdNegateExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = NEGATE_EX

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "NegateExpr")) return false
        var ok = b.consumeToken(NEGATE, pin = true)
        ok = ok && (GdExprParser.parseFrom(b, l, false, POSITION) || GdExprParser.parseFrom(b, l, optional, POSITION + 1))
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
