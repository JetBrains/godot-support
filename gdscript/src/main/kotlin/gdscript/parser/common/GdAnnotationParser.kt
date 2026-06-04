package gdscript.parser.common

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.ANNOTATION_PARAMS
import gdscript.psi.GdTypes.ANNOTATION_TYPE
import gdscript.psi.GdTypes.ANNOTATOR
import gdscript.psi.GdTypes.COMMA
import gdscript.psi.GdTypes.LRBR
import gdscript.psi.GdTypes.RRBR

interface GdAnnotationParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "Annotator")) return false
        if (!b.nextTokenIs(ANNOTATOR)) return optional

        val ok = b.mceAnyOf(ANNOTATION_TYPE, false, ANNOTATOR)
        b.pin()
        if (ok && b.nextTokenIs(LRBR)) {
            parseParams(b, l + 1)
        }

        return true
    }

    private fun parseParams(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "AnnotationParams")) return false
        b.enterSection(ANNOTATION_PARAMS)

        var ok = b.consumeToken(LRBR, pin = true)
        if (ok) {
            ok = GdExprParser.parse(b, l + 1, false)
            while (ok && b.passToken(COMMA)) {
                ok = GdExprParser.parse(b, l + 1, false)
            }
            GdRecovery.argumentList(b, ok)
            ok = b.consumeToken(RRBR) && ok
        }

        return b.exitSection(ok)
    }

}
