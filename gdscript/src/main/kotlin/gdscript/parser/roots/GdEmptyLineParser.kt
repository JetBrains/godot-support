package gdscript.parser.roots

import com.intellij.lang.PsiBuilder
import com.intellij.psi.TokenType
import gdscript.parser.GdBaseParser
import gdscript.psi.GdTypes.NEW_LINE
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdEmptyLineParser : GdBaseParser {

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        if (b.nextTokenIs(NEW_LINE)) {
            b.remapCurrentToken(TokenType.WHITE_SPACE)
            b.advanceLexer()
            return true
        }

        return false
    }

}
