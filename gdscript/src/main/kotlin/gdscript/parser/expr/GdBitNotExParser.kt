package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.BIT_NOT_EX
import gdscript.psi.GdTypes.NOT

// bitNot = "~" bitNot | is ;
object GdBitNotExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = BIT_NOT_EX

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "BitNotExpr")) return false
        var ok = b.consumeToken(NOT, pin = true)
        ok = ok && (
                GdExprParser.parseFrom(b, l, false, POSITION)
                        || GdExprParser.parseFrom(b, l, optional, POSITION + 1)
                )
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
