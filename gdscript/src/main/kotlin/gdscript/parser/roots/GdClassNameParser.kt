package gdscript.parser.roots

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.psi.GdTypes.*

class GdClassNameParser : GdBaseParser {

    constructor(builder: PsiBuilder): super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(CLASS_NAME)) return optional

        val m = mark()
        var ok = consumeToken(CLASS_NAME)
        ok = ok && mceIdentifier(CLASS_NAME_NMI)
        ok = ok && mceEndStmt()

        if (ok) m.done(CLASS_NAMING)
        else m.error("expected class_name")

        return ok || optional
    }

}
