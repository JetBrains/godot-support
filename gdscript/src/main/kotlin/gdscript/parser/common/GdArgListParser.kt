package gdscript.parser.common

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

object GdArgListParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        b.enterSection(ARG_LIST)

        var ok = b.pin(argExpr(b))
        while (b.consumeToken(COMMA, true)) {
            argExpr(b)
        }

        ok && GdRecovery.argumentList(b)
        ok = b.exitSection(ok)

        return ok || optional
    }

    private fun argExpr(b: GdPsiBuilder): Boolean {
        b.enterSection(ARG_EXPR)

        var ok = GdExprParser.parse(b)
        b.exitSection(ok)

        return ok
    }
}
