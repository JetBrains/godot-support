package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.psi.GdTypes.*

class GdLiteralExParser : GdBaseParser {

    companion object {
        lateinit var INSTANCE: GdLiteralExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(): Boolean {
        if (mcAnyOf(LITERAL_EX)) return true
        if (nextTokenIs(STRING)) {
            val m = mark()
            markToken(STRING_VAL)
            m.done(LITERAL_EX)
            return true
        }

        return false
    }

}
