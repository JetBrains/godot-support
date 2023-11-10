package gdscript.parser.common

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.psi.GdTypes

class GdReturnHintParser : GdBaseParser {

    companion object {
        lateinit var INSTANCE: GdReturnHintParser
    }

    constructor(builder: PsiBuilder) : super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(GdTypes.RET)) return optional
        val hint = mark()
        var ok = true
        advance() // RET

        val hintVal = mark()
        if (nextTokenIs(GdTypes.VOID)) {
            advance() // VOID
        } else {
            ok = ok && GdTypedParser.INSTANCE.typedVal(false)
        }

        hintVal.done(GdTypes.RETURN_HINT_VAL)

        if (ok) hint.done(GdTypes.RETURN_HINT)
        else hint.rollbackTo()

        return ok || optional
    }

}
