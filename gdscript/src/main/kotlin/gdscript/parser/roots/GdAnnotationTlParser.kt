package gdscript.parser.roots

import com.intellij.psi.TokenType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdAnnotationParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

object GdAnnotationTlParser : GdAnnotationParser {

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(ANNOTATOR)) return optional
        b.enterSection(ANNOTATION_TL)

        var ok = super.parse(b, optional)

        while (b.nextTokenIs(NEW_LINE)) {
            b.remapCurrentToken(TokenType.WHITE_SPACE)
            b.advance()
        }
        ok = ok && GdRecovery.topLevel(b)
        ok = ok && b.exitSection(ok)

        return ok
    }

}
