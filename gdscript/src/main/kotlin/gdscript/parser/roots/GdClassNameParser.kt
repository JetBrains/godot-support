package gdscript.parser.roots

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.psi.GdTypes.*

class GdClassNameParser : GdBaseParser {

    constructor(builder: PsiBuilder): super(builder)

    override fun parse(): Boolean {
        if (!nextTokenIs(CLASS_NAME)) return false

        val m = mark()
        var ok = consumeToken(CLASS_NAME)
        ok = ok && mcIdentifier(CLASS_NAME_NMI)
        ok = ok && mcEndStmt()
        if (!ok) {
            // TODO error
        }

        m.done(CLASS_NAMING)

        return true
    }

}
