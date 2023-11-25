package gdscript.parser.common

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdArgListParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        val argList = b.mark()
        var ok = true

        ok = ok && argExpr(b)
        if (!ok) {
            argList.rollbackTo()
            return optional
        }

        while (b.nextTokenIs(COMMA)) {
            b.advanceLexer() // comma
            argExpr(b)
        }

        GdRecovery.argumentList(b)
        argList.done(ARG_LIST)

        return true
    }

    private fun argExpr(b: GdPsiBuilder): Boolean {
        val expr = b.mark()
        var ok = true

        ok = ok && GdExprParser.parse(b)

        if (ok) expr.done(ARG_EXPR)
        else expr.rollbackTo()

        return ok
    }
}
