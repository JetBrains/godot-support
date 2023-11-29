package gdscript.parser.roots

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

object GdClassNameParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(CLASS_NAME)) return optional

        b.enterSection(CLASS_NAMING)
        var ok = b.consumeToken(CLASS_NAME, pin = true)

        ok = ok && b.mceIdentifier(CLASS_NAME_NMI)
        ok = ok && b.mceEndStmt()

        ok && GdRecovery.topLevel(b)
        ok = b.exitSection(ok)

        return ok
    }

}
