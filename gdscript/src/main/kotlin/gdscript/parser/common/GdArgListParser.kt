package gdscript.parser.common

import com.intellij.psi.TokenType
import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

object GdArgListParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "ArgList")) return false
        b.enterSection(ARG_LIST)

        // Allow leading whitespace-like layout tokens inside the parens
        skipWsInArgs(b)

        var ok = true
        // Empty list: immediate closing parenthesis
        if (!b.nextTokenIs(RRBR)) {
            ok = b.pin(argExpr(b, l + 1))

            // Parse ", expr" pairs. Newlines/indents after comma are ok.
            while (b.consumeToken(COMMA, true)) {
                skipWsInArgs(b)
                // Trailing comma before ")" is allowed
                if (b.nextTokenIs(RRBR)) break
                argExpr(b, l + 1)
            }
        }

        // Trailing layout before ")" is ok
        skipWsInArgs(b)

        // Recovery hook (side-effects) and close the section
        GdRecovery.argumentList(b)
        ok = b.exitSection(ok)
        return ok || optional
    }

    private fun skipWsInArgs(b: GdPsiBuilder) {
        // Treat NEW_LINE/INDENT/DEDENT as ordinary whitespace while in args
        while (b.nextTokenIs(NEW_LINE, INDENT, DEDENT)) {
            b.remapCurrentToken(TokenType.WHITE_SPACE)
            b.advance()
        }
    }

    private fun argExpr(b: GdPsiBuilder, l: Int): Boolean {
        b.recursionGuard(l + 1, "ArgExpr")
        b.enterSection(ARG_EXPR)
        val ok = GdExprParser.parse(b, l + 1, false)
        b.exitSection(ok)
        return ok
    }
}