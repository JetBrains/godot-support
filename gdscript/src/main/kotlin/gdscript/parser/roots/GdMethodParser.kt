package gdscript.parser.roots

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdParamListParser
import gdscript.parser.common.GdReturnHintParser
import gdscript.parser.expr.GdLiteralExParser
import gdscript.parser.recovery.GdRecovery
import gdscript.parser.stmt.GdStmtParser
import gdscript.psi.GdTypes.*

object GdMethodParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "Method")) return false
        b.enterSection(METHOD_DECL_TL)
        var ok = markers(b)

        if (!ok && !b.nextTokenIs(FUNC)) {
            b.exitSection(false)
            return optional
        }

        ok = b.consumeToken(FUNC, pin = true)
        ok = ok && b.mceIdentifier(METHOD_ID_NMI)
        ok = ok && b.consumeToken(LRBR)

        ok = ok && GdParamListParser.parse(b, l + 1, true)

        ok = ok && b.consumeToken(RRBR)
        ok = ok && GdReturnHintParser.parse(b, l + 1, true)
        ok = ok && b.consumeToken(COLON)
        ok = ok && GdStmtParser.parse(b, l + 1)

        GdRecovery.topLevel(b)

        return b.exitSection(ok)
    }

    private fun markers(b: GdPsiBuilder): Boolean {
        var marked = false

        while (b.mcToken(
                METHOD_SPECIFIER,
                REMOTE,
                MASTER,
                PUPPET,
                REMOTESYNC,
                MASTERSYNC,
                PUPPETSYNC,
                STATIC,
                VARARG,
            )
        ) {
            marked = true
        }

        return marked
    }

}
