package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.MINUS
import gdscript.psi.GdTypes.PLUS
import gdscript.psi.GdTypes.SIGN_EX

// sign = ( "-" | "+" ) sign | bitNot ;
object GdSignExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = SIGN_EX

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "SignExpr")) return false
        var ok = b.passToken(PLUS, MINUS)
        b.pin(ok)
        ok = ok && (
                GdExprParser.parseFrom(b, l, false, POSITION)
                        || GdExprParser.parseFrom(b, l, optional, POSITION + 1)
                )
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
