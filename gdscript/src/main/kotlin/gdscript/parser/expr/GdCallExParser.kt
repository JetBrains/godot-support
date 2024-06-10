package gdscript.parser.expr

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdArgListParser
import gdscript.psi.GdTypes.*

/*
call
    = (attribute [ "(" [ argList ] ")" ])
    TODO - not implemented
    | "." IDENTIFIER "(" [ argList ] ")"
    | "$" ( STRING | IDENTIFIER { '/' IDENTIFIER } );
 */
object GdCallExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = CALL_EX
    override val isNested = true

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "CallExpr")) return false
        var ok = b.consumeToken(LRBR, pin = true)
        ok = ok && GdArgListParser.parse(b, l + 1, true)
        ok = ok && b.consumeToken(RRBR)

        return ok
    }

}
