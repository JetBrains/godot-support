package gdscript.parser.common

import gdscript.GdKeywords.ARRAY
import gdscript.GdKeywords.DICTIONARY
import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.ASSIGN_TYPED
import gdscript.psi.GdTypes.CEQ
import gdscript.psi.GdTypes.COLON
import gdscript.psi.GdTypes.COMMA
import gdscript.psi.GdTypes.DOT
import gdscript.psi.GdTypes.EQ
import gdscript.psi.GdTypes.IDENTIFIER
import gdscript.psi.GdTypes.LSBR
import gdscript.psi.GdTypes.RSBR
import gdscript.psi.GdTypes.TYPED
import gdscript.psi.GdTypes.TYPED_VAL
import gdscript.psi.GdTypes.TYPE_HINT
import gdscript.psi.GdTypes.TYPE_HINT_REF

object GdTypedParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "Typed")) return false
        if (!b.nextTokenIs(COLON)) return optional

        b.enterSection(TYPED)

        var ok = b.consumeToken(COLON)
        ok = ok && typedVal(b, l + 1, false)

        return b.exitSection(ok)
    }

    fun parseWithAssignTypedAndExpr(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "TypedAndExpr")) return false
        val m = b.mark()
        var ok = true
        if (b.nextTokenIs(CEQ)) {
            ok = ok && b.mcToken(ASSIGN_TYPED, CEQ)
            if (ok) {
                ok = ok && GdExprParser.parse(b, l + 1)
                if (!ok) b.error("EXPRESSION")
            }
        } else if (b.nextTokenIs(COLON)) {
            ok = ok && parse(b, l + 1, optional)
            if (ok && b.mcToken(ASSIGN_TYPED, EQ)) {
                if (ok) {
                    ok = ok && GdExprParser.parse(b, l + 1)
                    if (!ok) b.error("EXPRESSION")
                }
            }
        } else if (b.mcToken(ASSIGN_TYPED, EQ)) {
            if (ok) {
                ok = ok && GdExprParser.parse(b, l + 1)
                if (!ok) b.error("EXPRESSION")
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

        // Capture base type name (first IDENTIFIER token text) before consuming it via parseTypeHint
        val baseTypeName = if (b.nextTokenIs(IDENTIFIER)) b.tokenText else null

        var ok = parseTypeHint(b, l + 1)
        if (ok && b.consumeToken(LSBR, true)) {
            var paramCount = 0
            // Support either single param (Array[T]) or exactly two params (Dictionary[K, V])
            ok = ok && parseTypeHint(b, l + 1)
            if (ok) paramCount++
            if (ok && b.passToken(COMMA)) {
                ok = ok && parseTypeHint(b, l + 1)
                if (ok) paramCount++
                // Disallow more than two parameters
                if (ok && b.nextTokenIs(COMMA)) {
                    ok = false
                    b.error("Only two type parameters are supported")
                }
            }
            ok = ok && b.consumeToken(RSBR, true)

            // Enforce parameter counts for known generic containers
            if (ok && baseTypeName != null) {
                when (baseTypeName) {
                    ARRAY -> if (paramCount != 1) {
                        ok = false
                        b.error("Array expects exactly one type parameter")
                    }
                    DICTIONARY -> if (paramCount != 2) {
                        ok = false
                        b.error("Dictionary expects exactly two type parameters")
                    }
                }
            }
        }

        if (ok) typedVal.done(TYPED_VAL)
        else typedVal.rollbackTo()

        return ok || optional
    }

    private fun parseTypeHint(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "TypeHint")) return false
        if (!b.nextTokenIs(IDENTIFIER)) return false

        b.enterSection(TYPE_HINT)

        var ok = b.mceIdentifier(TYPE_HINT_REF)
        b.pin(ok)
        while (ok && b.passToken(DOT)) {
            ok = ok && b.mceIdentifier(TYPE_HINT_REF)
        }

        return b.exitSection(ok)
    }

}
