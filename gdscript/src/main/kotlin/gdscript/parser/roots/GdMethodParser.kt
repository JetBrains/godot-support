package gdscript.parser.roots

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdParamListParser
import gdscript.parser.common.GdReturnHintParser
import gdscript.parser.recovery.GdRecovery
import gdscript.parser.stmt.GdStmtParser
import gdscript.psi.GdTypes.*

object GdMethodParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        b.enterSection(METHOD_DECL_TL)
        var ok = markers(b)

        if (!ok && !b.nextTokenIs(FUNC)) {
            b.exitSection(false)
            return optional
        }

        ok = b.consumeToken(FUNC, pin = true)
        ok = ok && b.mceIdentifier(METHOD_ID_NMI)
        ok = ok && b.consumeToken(LRBR)

        ok = ok && GdParamListParser.parse(b, true)

        ok = ok && b.consumeToken(RRBR)
        ok = ok && GdReturnHintParser.parse(b, true)
        ok = ok && b.consumeToken(COLON)
        ok = ok && GdStmtParser.parse(b)

        GdRecovery.topLevel(b)

        return b.exitSection(ok)
    }

    private fun markers(b: GdPsiBuilder): Boolean {
        var marked = false

        while (b.passToken(
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
