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

        var ok = b.pin(argExpr(b, l + 1))
        while (b.consumeToken(COMMA, true)) {
            argExpr(b, l + 1)
        }

        ok && GdRecovery.argumentList(b)
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
