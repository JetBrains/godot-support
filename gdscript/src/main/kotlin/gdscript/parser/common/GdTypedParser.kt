package gdscript.parser.common

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.mcAnyOf
import gdscript.utils.PsiBuilderUtil.mceIdentifier
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdTypedParser : GdBaseParser {

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(COLON)) return optional

        var ok = true
        val typed = b.mark()
        b.advanceLexer() // Colon

        ok = ok && typedVal(b, false)

        if (ok) {
            typed.done(TYPED)
        } else {
            typed.rollbackTo()
        }

        return true
    }

    fun parseWithAssignTypedAndExpr(b: PsiBuilder, optional: Boolean): Boolean {
        var ok = true
        if (b.nextTokenIs(CEQ)) {
            ok = ok && b.mcAnyOf(ASSIGN_TYPED, CEQ)
            ok = ok && GdExprParser.parse(b)
        } else if (b.nextTokenIs(COLON)) {
            ok = ok && parse(b, optional)
            if (ok && b.mcAnyOf(ASSIGN_TYPED, EQ)) {
                ok = ok && GdExprParser.parse(b)
            }
        } else if (b.mcAnyOf(ASSIGN_TYPED, EQ)) {
            ok = ok && GdExprParser.parse(b)
        } else if (!optional) {
            return false
        }

        return true
    }

    fun typedVal(b: PsiBuilder, optional: Boolean): Boolean {
        val typedVal = b.mark()

        var ok = parseTypeHint(b)
        if (ok && b.nextTokenIs(LSBR)) {
            b.advanceLexer()
            ok = parseTypeHint(b)
            ok = ok && b.consumeToken(RSBR, true)
        }

        if (ok) typedVal.done(TYPED_VAL)
        else typedVal.rollbackTo()

        return ok || optional
    }

    private fun parseTypeHint(b: PsiBuilder): Boolean {
        if (!b.nextTokenIs(IDENTIFIER)) return false

        val hint = b.mark()

        var ok = b.mceIdentifier(TYPE_HINT_NM)
        while (ok && b.nextTokenIs(DOT)) {
            b.advanceLexer()
            ok = b.mceIdentifier(TYPE_HINT_NM)
        }

        hint.done(TYPE_HINT)

        return true
    }

}
