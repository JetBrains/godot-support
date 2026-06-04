package gdscript.parser.common

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.ARG_EXPR
import gdscript.psi.GdTypes.ARG_LIST
import gdscript.psi.GdTypes.COMMA
import gdscript.psi.GdTypes.END_STMT
import gdscript.psi.GdTypes.LRBR
import gdscript.psi.GdTypes.NEW_LINE
import gdscript.psi.GdTypes.RRBR

object GdArgListParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "ArgList")) return false
        if (!b.nextTokenIs(LRBR)) {
            if (!optional) b.consumeToken(LRBR)
            return optional
        }

        b.enterSection(ARG_LIST)

        var ok = b.consumeToken(LRBR, pin = true)
        if (ok && !b.nextTokenIs(RRBR)) {
            ok = argExpr(b, l + 1)
            while (ok && b.consumeToken(COMMA, true)) {
                if (b.nextTokenIs(RRBR)) break
                ok = argExpr(b, l + 1)
            }
        }

        GdRecovery.argumentList(b, ok)

        // A lambda argument with a suite can leave a newline before the closing parenthesis.
        if (ok && b.nextTokenIs(NEW_LINE)) {
            val m = b.mark()
            b.advance()
            m.done(END_STMT)
        }

        val hasRightParen = b.consumeToken(RRBR)
        ok = ok && hasRightParen
        ok = b.exitSection(ok)

        return ok || optional
    }

    private fun argExpr(b: GdPsiBuilder, l: Int): Boolean {
        b.recursionGuard(l + 1, "ArgExpr")
        b.enterSection(ARG_EXPR)

        val ok = GdExprParser.parse(b, l + 1, false)
        b.exitSection(ok)

        return ok
    }
}
