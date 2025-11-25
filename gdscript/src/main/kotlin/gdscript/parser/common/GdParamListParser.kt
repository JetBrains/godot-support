package gdscript.parser.common

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdLiteralExParser
import gdscript.psi.GdTypes.*

object GdParamListParser : GdBaseParser {
    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "ParamList")) return false
        // Allow start with IDENTIFIER or DOTDOTDOT
        if (!b.nextTokenIs(DOTDOTDOT) && !GdLiteralExParser.checkExtendedRefId(b)) return optional

        var ok = true
        val paramList = b.mark()

        while (ok && (b.nextTokenIs(DOTDOTDOT) || GdLiteralExParser.checkExtendedRefId(b))) {
            // Peek whether this parameter is variadic before consuming tokens in param()
            val isRestNext = b.nextTokenIs(DOTDOTDOT)
            ok = param(b, l + 1)

            // Variadic must be last
            if (isRestNext) {
                if (b.nextTokenIs(COMMA)) {
                    b.error("Variadic parameter must be the last parameter", false)
                    // consume the comma to avoid infinite loop/cascading errors
                    b.passToken(COMMA)
                    // best-effort: stop parsing further params
                }
                break
            }

            if (!b.passToken(COMMA)) break
        }

        paramList.done(PARAM_LIST)

        return true
    }

    private fun param(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "Param")) return false
        val param = b.mark()
        var ok: Boolean

        val isRest = b.passToken(DOTDOTDOT)

        ok = GdLiteralExParser.parseExtendedRefId(b, VAR_NMI)

        if (isRest) {
            // Optional type hint allowed, but no default value
            ok = ok && GdTypedParser.parse(b, l + 1, true)
            if (b.nextTokenIs(CEQ, EQ)) {
                b.error("Variadic parameter cannot have a default value", false)
                // consume '=' to prevent the rest of the list from breaking badly
                b.passToken(CEQ)
                b.passToken(EQ)
            }
        } else {
            ok = ok && GdTypedParser.parseWithAssignTypedAndExpr(b, l + 1, true)
        }

        param.done(PARAM)

        return ok
    }
}
