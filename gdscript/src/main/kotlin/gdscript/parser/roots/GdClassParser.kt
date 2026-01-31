package gdscript.parser.roots

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.GdRootParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.CLASS
import gdscript.psi.GdTypes.CLASS_DECL_TL
import gdscript.psi.GdTypes.CLASS_NAME_NMI
import gdscript.psi.GdTypes.COLON
import gdscript.psi.GdTypes.DEDENT
import gdscript.psi.GdTypes.INDENT
import gdscript.psi.GdTypes.NEW_LINE

object GdClassParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "Class")) return false
        if (!b.nextTokenIs(CLASS)) return optional

        b.enterSection(CLASS_DECL_TL)
        var ok = b.consumeToken(CLASS, pin = true)

        ok = ok && b.mceIdentifier(CLASS_NAME_NMI)
        ok = ok && GdInheritanceParser.parseFromClass(b, l + 1, true, true)
        ok = ok && b.consumeToken(COLON)
        ok = ok && b.consumeToken(NEW_LINE)
        ok = ok && b.consumeToken(INDENT)

        while (
            ok &&
            (GdInheritanceParser.parse(b, l + 1)
                || GdRootParser.topLevelParsers.any { it.parse(b, l + 1) })
        ) {
        }
        ok = ok && b.consumeToken(DEDENT, true)

        b.pinned() && GdRecovery.topLevel(b)

        return b.exitSection(ok) || optional
    }

}
