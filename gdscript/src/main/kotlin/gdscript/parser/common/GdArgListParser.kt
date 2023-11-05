package gdscript.parser.common

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

class GdArgListParser : GdBaseParser {

    companion object {
        lateinit var INSTANCE: GdArgListParser
    }

    constructor(builder: PsiBuilder) : super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        val argList = mark()
        var ok = true

        ok = ok && argExpr()
        if (!ok) {
            argList.rollbackTo()
            return optional
        }

        while (nextTokenIs(COMMA)) {
            argExpr()
        }

        GdRecovery.argumentList()
        argList.done(ARG_LIST)

        return true
    }

    private fun argExpr(): Boolean {
        val expr = mark()
        var ok = true

        ok = ok && GdExprParser.INSTANCE.parse()

        if (ok) expr.done(ARG_EXPR)
        else expr.rollbackTo()

        return ok
    }
}
