package gdscript.parser.common

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

object GdParamListParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "ParamList")) return false
        if (!b.nextTokenIs(VAR, IDENTIFIER)) return optional
        var ok = true
        val paramList = b.mark()

        while (ok && b.nextTokenIs(VAR, IDENTIFIER)) {
            ok = param(b, l + 1)
            if (!b.passToken(COMMA)) break
        }

        paramList.done(PARAM_LIST)

        return true
    }

    private fun param(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "Param")) return false
        val param = b.mark()
        var ok = true

        b.passToken(VAR)
        ok = ok && b.mceIdentifier(VAR_NMI)
        ok = ok && GdTypedParser.parseWithAssignTypedAndExpr(b, l + 1, true)

        param.done(PARAM)

        return ok
    }
}
