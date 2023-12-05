package gdscript.parser.common

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*

object GdTypedParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "Typed")) return false
        if (!b.nextTokenIs(COLON)) return optional

        b.enterSection(TYPED)

        var ok = b.consumeToken(COLON)
        ok = ok && typedVal(b, l + 1, false)

        b.exitSection(ok)

        return ok
    }

    fun parseWithAssignTypedAndExpr(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "TypedAndExpr")) return false
        val m = b.mark()
        var ok = true
        if (b.nextTokenIs(CEQ)) {
            ok = ok && b.mcToken(ASSIGN_TYPED, CEQ)
            if (ok) {
                ok = ok && GdExprParser.parse(b, l + 1)
                if (!ok) b.error("expression")
            }
        } else if (b.nextTokenIs(COLON)) {
            ok = ok && parse(b, l + 1, optional)
            if (ok && b.mcToken(ASSIGN_TYPED, EQ)) {
                if (ok) {
                    ok = ok && GdExprParser.parse(b, l + 1)
                    if (!ok) b.error("expression")
                }
            }
        } else if (b.mcToken(ASSIGN_TYPED, EQ)) {
            if (ok) {
                ok = ok && GdExprParser.parse(b, l + 1)
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

    fun typedVal(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "TypedVal")) return false
        val typedVal = b.mark()

        var ok = parseTypeHint(b, l + 1)
        if (ok && b.consumeToken(LSBR, true)) {
            ok = ok && parseTypeHint(b, l + 1)
            ok = ok && b.consumeToken(RSBR, true)
        }

        if (ok) typedVal.done(TYPED_VAL)
        else typedVal.rollbackTo()

        return ok || optional
    }

    private fun parseTypeHint(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "TypeHint")) return false
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
