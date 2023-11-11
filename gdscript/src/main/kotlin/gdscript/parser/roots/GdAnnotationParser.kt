package gdscript.parser.roots

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.expr.GdLiteralExParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.markToken
import gdscript.utils.PsiBuilderUtil.nextTokenIs

class GdAnnotationParser : GdBaseParser() {

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(ANNOTATOR)) return optional

        val m = b.mark()
        var ok = b.markToken(ANNOTATION_TYPE)
        if (ok && b.nextTokenIs(LRBR)) {
            b.advanceLexer()
            ok = ok && parseParams(b)
            ok = ok && b.consumeToken(RRBR)
        }
        GdRecovery.topLevel(b)

        m.done(ANNOTATION_TL)
        return true
    }

    private fun parseParams(b: PsiBuilder): Boolean {
        val params = b.mark()

        var ok = GdLiteralExParser.INSTANCE.parseAndMark(b)
        while (ok && b.nextTokenIs(COMMA)) {
            ok = GdLiteralExParser.INSTANCE.parseAndMark(b)
        }
        GdRecovery.argumentList(b)
        params.done(ANNOTATION_PARAMS)

        return ok
    }

}
