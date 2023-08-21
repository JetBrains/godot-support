package gdscript.parser.common

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.psi.GdTypes.*

class GdTypedParser : GdBaseParser {

    companion object {
        lateinit var INSTANCE: GdTypedParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(COLON)) return optional

        val typed = mark()
        advance() // Colon
        val typedVal = mark()

        var ok = parseTypeHint()
        if (ok && nextTokenIs(LSBR)) {
            advance()
            ok = parseTypeHint()
            ensureNextTokenIs(RSBR)
        }

        if (ok) {
            typedVal.done(TYPED_VAL)
            typed.done(TYPED)
        } else {
            typed.rollbackTo()
        }

        return true
    }

    private fun parseTypeHint(): Boolean {
        val hint = mark()
        var ok = mceIdentifier(TYPE_HINT_NM)
        while (ok && nextTokenIs(DOT)) {
            advance()
            ok = mceIdentifier(TYPE_HINT_NM)
        }

        hint.done(TYPE_HINT)
        return ok
    }

}
