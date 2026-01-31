package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.ELSE
import gdscript.psi.GdTypes.IF
import gdscript.psi.GdTypes.TERNARY_EX

// logicOr [ "if" logicOr "else" logicOr ] ;
object GdTernaryExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = TERNARY_EX
    override val isNested = true

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "TernaryExpr")) return false

        var ok = b.consumeToken(IF, pin = true)
        ok = ok && GdExprParser.parseFrom(b, l, optional, POSITION + 1)
        b.errorPin(ok, "expression")
        ok = ok && b.consumeToken(ELSE)
        ok = ok && GdExprParser.parseFrom(b, l, optional, POSITION + 1)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
