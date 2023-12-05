package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.AWAIT
import gdscript.psi.GdTypes.AWAIT_EX

object GdAwaitExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = AWAIT_EX

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "AwaitExpr")) return false
        if (!b.nextTokenIs(AWAIT)) return optional

        var ok = b.consumeToken(AWAIT, pin = true)
        ok = ok && GdExprParser.parse(b, l + 1)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
