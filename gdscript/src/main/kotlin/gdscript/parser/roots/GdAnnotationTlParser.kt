package gdscript.parser.roots

import com.intellij.psi.TokenType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdAnnotationParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.ANNOTATION_TL
import gdscript.psi.GdTypes.ANNOTATOR
import gdscript.psi.GdTypes.NEW_LINE

object GdAnnotationTlParser : GdAnnotationParser {

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "AnnotationTl")) return false
        if (!b.nextTokenIs(ANNOTATOR)) return optional
        b.enterSection(ANNOTATION_TL)

        var ok = super.parse(b, l + 1, optional)

        while (b.nextTokenIs(NEW_LINE)) {
            b.remapCurrentToken(TokenType.WHITE_SPACE)
            b.advance()
        }
        ok = ok && GdRecovery.topLevel(b)
        ok = ok && b.exitSection(ok)

        return ok
    }

}
