package gdscript.parser.common

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdLiteralExParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

interface GdAnnotationParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(ANNOTATOR)) return optional

        var ok = b.mceAnyOf(ANNOTATION_TYPE, false, ANNOTATOR)
        b.pin()
        if (ok && b.consumeToken(LRBR, true)) {
            ok = ok && parseParams(b)
            ok = ok && b.consumeToken(RRBR, true)
        }

        return ok
    }

    private fun parseParams(b: GdPsiBuilder): Boolean {
        b.enterSection(ANNOTATION_PARAMS)

        var ok = GdLiteralExParser.parseAndMark(b)
        while (ok && b.consumeToken(COMMA, true)) {
            ok = GdLiteralExParser.parseAndMark(b)
        }
        ok && GdRecovery.argumentList(b)
        b.exitSection(ok)

        return ok
    }

}
