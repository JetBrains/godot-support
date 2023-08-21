package gdscript.parser.roots

import com.intellij.lang.PsiBuilder
import com.intellij.psi.TokenType
import gdscript.parser.GdBaseParser
import gdscript.psi.GdTypes.*

class GdEmptyLineParser : GdBaseParser {

    constructor(builder: PsiBuilder): super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (nextTokenIs(NEW_LINE)) {
            builder.remapCurrentToken(TokenType.WHITE_SPACE)
            builder.advanceLexer()
            return true
        }

        return false
    }

}
