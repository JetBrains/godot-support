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

        // Leading whitespace should belong to CALL_EX, not ARG_LIST
        skipArgWs(b)
        b.enterSection(ARG_LIST)

        var ok = b.pin(argExpr(b, l + 1))
        while (b.consumeToken(COMMA, true)) {
            // Inter-argument whitespace stays inside ARG_LIST
            skipArgWs(b)
            argExpr(b, l + 1)
            skipArgWs(b)
        }

        ok && GdRecovery.argumentList(b)
        ok = b.exitSection(ok)

        // Trailing whitespace should also belong to CALL_EX, not ARG_LIST
        skipArgWs(b)

        return ok || optional
    }

    private fun skipArgWs(b: GdPsiBuilder) {
        // Remap and actively consume structural whitespace inside argument lists
        // so expression parsing does not choke on a pending WHITE_SPACE token.
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
