package gdscript.parser.roots

import com.intellij.psi.TokenType
import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.NEW_LINE

object GdEmptyLineParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (b.nextTokenIs(NEW_LINE)) {
            b.remapCurrentToken(TokenType.WHITE_SPACE)
            b.advance()
            return true
        }

        return false
    }

}
