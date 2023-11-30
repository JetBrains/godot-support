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
        if (ok && b.passToken(LRBR)) {
            ok = ok && parseParams(b)
            ok = ok && b.consumeToken(RRBR)
        }

        return true
    }

    private fun parseParams(b: GdPsiBuilder): Boolean {
        b.enterSection(ANNOTATION_PARAMS)

        var ok = GdLiteralExParser.parseAndMark(b)
        while (ok && b.passToken(COMMA)) {
            ok = GdLiteralExParser.parseAndMark(b)
        }
        GdRecovery.argumentList(b, ok)

        return b.exitSection(ok)
    }

}
