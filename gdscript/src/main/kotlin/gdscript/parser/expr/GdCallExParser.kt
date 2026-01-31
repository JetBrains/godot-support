package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdArgListParser
import gdscript.psi.GdTypes.CALL_EX
import gdscript.psi.GdTypes.END_STMT
import gdscript.psi.GdTypes.LRBR
import gdscript.psi.GdTypes.NEW_LINE
import gdscript.psi.GdTypes.RRBR

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
        // If a lambda argument ended with a suite, the lexer/parser may leave a NEW_LINE
        // just before the closing ')'. Wrap it as END_STMT so the lambda "statement"
        // inside argument list properly terminates before the bracket.
        if (ok && b.nextTokenIs(NEW_LINE)) {
            val m = b.mark()
            b.advance()
            m.done(END_STMT)
        }
        ok = ok && b.consumeToken(RRBR)

        return ok
    }

}
