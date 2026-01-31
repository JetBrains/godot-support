package gdscript.parser.roots

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.CLASS_NAME
import gdscript.psi.GdTypes.CLASS_NAME_NMI
import gdscript.psi.GdTypes.CLASS_NAMING

object GdClassNameParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "ClassName")) return false
        if (!b.nextTokenIs(CLASS_NAME)) return optional

        b.enterSection(CLASS_NAMING)
        var ok = b.consumeToken(CLASS_NAME, pin = true)

        ok = ok && b.mceIdentifier(CLASS_NAME_NMI)
        ok = ok && b.mceEndStmt(true)

        GdRecovery.topLevel(b)
        ok = b.exitSection(ok)

        return ok
    }

}
