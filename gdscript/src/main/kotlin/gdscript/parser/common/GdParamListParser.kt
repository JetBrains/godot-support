package gdscript.parser.common

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

object GdParamListParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(VAR, IDENTIFIER)) return optional
        var ok = true
        val paramList = b.mark()

        while (ok && b.nextTokenIs(VAR, IDENTIFIER)) {
            ok = param(b)
            if (!b.passToken(COMMA)) break
        }

        paramList.done(PARAM_LIST)

        return true
    }

    private fun param(b: GdPsiBuilder): Boolean {
        val param = b.mark()
        var ok = true

        b.passToken(VAR)
        ok = ok && b.mceIdentifier(VAR_NMI)
        ok = ok && GdTypedParser.parseWithAssignTypedAndExpr(b, true)

        param.done(PARAM)

        return ok
    }
}
