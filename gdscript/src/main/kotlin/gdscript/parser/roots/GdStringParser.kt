package gdscript.parser.roots

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

object GdStringParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.nextTokenIs(STRING)) return false

        val m = b.mark()
        b.advance()
        m.done(LITERAL_EX)

        return true
    }
}
