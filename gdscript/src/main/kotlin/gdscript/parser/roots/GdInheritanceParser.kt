package gdscript.parser.roots

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

object GdInheritanceParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(EXTENDS)) return optional

        b.enterSection(INHERITANCE)
        var ok = b.consumeToken(EXTENDS, pin = true)

        b.enterSection(INHERITANCE_ID)

        ok = ok && b.mceAnyOf(INHERITANCE_ID_NM, false, IDENTIFIER, STRING)
        while (ok && b.consumeToken(DOT)) {
            ok = b.mceAnyOf(INHERITANCE_SUB_ID_NM, false, IDENTIFIER)
        }

        ok = b.exitSection(ok) // INHERITANCE_ID
        ok = ok && b.mceEndStmt()

        GdRecovery.topLevel(b)
        ok = b.exitSection(ok) // INHERITANCE

        return ok
    }

}
