package gdscript.parser.common

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.expr.GdLiteralExParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.markToken
import gdscript.utils.PsiBuilderUtil.nextTokenIs

interface GdAnnotationParser : GdBaseParser {

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(ANNOTATOR)) return optional

        var ok = b.markToken(ANNOTATION_TYPE)
        if (ok && b.nextTokenIs(LRBR)) {
            b.advanceLexer()
            ok = ok && parseParams(b)
            ok = ok && b.consumeToken(RRBR, true)
        }

        return true
    }

    private fun parseParams(b: PsiBuilder): Boolean {
        val params = b.mark()

        var ok = GdLiteralExParser.parseAndMark(b)
        while (ok && b.nextTokenIs(COMMA)) {
            b.advanceLexer() // comma
            ok = GdLiteralExParser.parseAndMark(b)
        }
        GdRecovery.argumentList(b)
        params.done(ANNOTATION_PARAMS)

        return ok
    }

}
