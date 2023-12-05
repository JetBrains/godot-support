package gdscript.parser.common

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdLiteralExParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

interface GdAnnotationParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "Annotator")) return false
        if (!b.nextTokenIs(ANNOTATOR)) return optional

        var ok = b.mceAnyOf(ANNOTATION_TYPE, false, ANNOTATOR)
        b.pin()
        if (ok && b.passToken(LRBR)) {
            ok && parseParams(b, l + 1)
            ok = ok && b.consumeToken(RRBR)
        }

        return true
    }

    private fun parseParams(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "AnnotationParams")) return false
        b.enterSection(ANNOTATION_PARAMS)

        var ok = GdLiteralExParser.parseAndMark(b, l + 1)
        while (ok && b.passToken(COMMA)) {
            ok = GdLiteralExParser.parseAndMark(b, l + 1)
        }
        GdRecovery.argumentList(b, ok)

        return b.exitSection(ok)
    }

}
