package gdscript.parser.roots

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.DOT
import gdscript.psi.GdTypes.EXTENDS
import gdscript.psi.GdTypes.IDENTIFIER
import gdscript.psi.GdTypes.INHERITANCE
import gdscript.psi.GdTypes.INHERITANCE_ID
import gdscript.psi.GdTypes.INHERITANCE_ID_NM
import gdscript.psi.GdTypes.INHERITANCE_SUB_ID_NM
import gdscript.psi.GdTypes.STRING

object GdInheritanceParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        return this.parseFromClass(b, l, optional)
    }

    fun parseFromClass(b: GdPsiBuilder, l: Int, optional: Boolean, fromClass: Boolean = false): Boolean {
        if (!b.recursionGuard(l, "Inheritance")) return false
        if (!b.nextTokenIs(EXTENDS)) return optional

        b.enterSection(INHERITANCE)
        var ok = b.consumeToken(EXTENDS, pin = true)

        b.enterSection(INHERITANCE_ID)

        ok = ok && b.mceAnyOf(INHERITANCE_ID_NM, false, IDENTIFIER, STRING)
        while (ok && b.passToken(DOT)) {
            ok = b.mceAnyOf(INHERITANCE_SUB_ID_NM, false, IDENTIFIER)
        }

        ok = b.exitSection(ok) // INHERITANCE_ID
        if (!fromClass) {
            ok = ok && b.mceEndStmt()
            GdRecovery.topLevel(b)
        }

        ok = b.exitSection(ok) // INHERITANCE

        return ok
    }

}
