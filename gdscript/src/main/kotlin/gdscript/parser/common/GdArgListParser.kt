package gdscript.parser.common

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.nextTokenIs

class GdArgListParser : GdBaseParser {

    companion object {
        lateinit var INSTANCE: GdArgListParser
    }

    constructor() {
        INSTANCE = this
    }

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        val argList = b.mark()
        var ok = true

        ok = ok && argExpr(b)
        if (!ok) {
            argList.rollbackTo()
            return optional
        }

        while (b.nextTokenIs(COMMA)) {
            argExpr(b)
        }

        GdRecovery.argumentList(b)
        argList.done(ARG_LIST)

        return true
    }

    private fun argExpr(b: PsiBuilder): Boolean {
        val expr = b.mark()
        var ok = true

        ok = ok && GdExprParser.INSTANCE.parse(b)

        if (ok) expr.done(ARG_EXPR)
        else expr.rollbackTo()

        return ok
    }
}
