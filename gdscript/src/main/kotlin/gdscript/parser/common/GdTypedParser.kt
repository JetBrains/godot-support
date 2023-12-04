package gdscript.parser.common

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*

object GdTypedParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(COLON)) return optional

        b.enterSection(TYPED)

        var ok = b.consumeToken(COLON)
        ok = ok && typedVal(b, false)

        b.exitSection(ok)

        return ok
    }

    fun parseWithAssignTypedAndExpr(b: GdPsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        if (b.nextTokenIs(CEQ)) {
            ok = ok && b.mcToken(ASSIGN_TYPED, CEQ)
            if (ok) {
                ok = ok && GdExprParser.parse(b)
                if (!ok) b.error("expression")
            }
        } else if (b.nextTokenIs(COLON)) {
            ok = ok && parse(b, optional)
            if (ok && b.mcToken(ASSIGN_TYPED, EQ)) {
                if (ok) {
                    ok = ok && GdExprParser.parse(b)
                    if (!ok) b.error("expression")
                }
            }
        } else if (b.mcToken(ASSIGN_TYPED, EQ)) {
            if (ok) {
                ok = ok && GdExprParser.parse(b)
                if (!ok) b.error("expression")
            }
        } else if (!optional) {
            m.drop()
            return false
        }

        if (ok || optional) m.drop()
        else m.rollbackTo()

        return ok || optional
    }

    fun typedVal(b: GdPsiBuilder, optional: Boolean): Boolean {
        val typedVal = b.mark()

        var ok = parseTypeHint(b)
        if (ok && b.consumeToken(LSBR, true)) {
            ok = ok && parseTypeHint(b)
            ok = ok && b.consumeToken(RSBR, true)
        }

        if (ok) typedVal.done(TYPED_VAL)
        else typedVal.rollbackTo()

        return ok || optional
    }

    private fun parseTypeHint(b: GdPsiBuilder): Boolean {
        if (!b.nextTokenIs(IDENTIFIER)) return false

        b.enterSection(TYPE_HINT)

        var ok = b.mceIdentifier(TYPE_HINT_NM)
        while (ok && b.consumeToken(DOT, true)) {
            ok = ok && b.mceIdentifier(TYPE_HINT_NM)
        }

        b.exitSection(ok)

        return ok
    }

}
