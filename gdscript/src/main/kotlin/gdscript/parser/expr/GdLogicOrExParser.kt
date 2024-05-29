package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

// logicAnd { ( "or" | "||" ) logicAnd } ;
object GdLogicOrExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = LOGIC_EX

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "LogicOrExpr")) return false
        var ok = b.consumeToken(OROR, pin = true)
        ok = ok && GdExprParser.parseFrom(b, l, optional, POSITION + 1)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
