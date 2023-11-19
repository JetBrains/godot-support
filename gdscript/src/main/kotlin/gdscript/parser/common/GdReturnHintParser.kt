package gdscript.parser.common

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.psi.GdTypes
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdReturnHintParser : GdBaseParser {

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(GdTypes.RET)) return optional
        val hint = b.mark()
        var ok = true
        b.advanceLexer() // RET

        val hintVal = b.mark()
        if (b.nextTokenIs(GdTypes.VOID)) {
            b.advanceLexer() // VOID
        } else {
            ok = ok && GdTypedParser.typedVal(b, false)
        }

        hintVal.done(GdTypes.RETURN_HINT_VAL)

        if (ok) hint.done(GdTypes.RETURN_HINT)
        else hint.rollbackTo()

        return ok || optional
    }

}
