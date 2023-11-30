package gdscript.parser.roots

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdParamListParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

object GdSignalParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(SIGNAL)) return optional

        b.enterSection(SIGNAL_DECL_TL)
        var ok = b.consumeToken(SIGNAL, pin = true)
        ok = ok && b.mceIdentifier(SIGNAL_ID_NMI)

        if (ok && b.passToken(LRBR)) {
            ok = ok && GdParamListParser.parse(b, false)
            ok = ok && b.consumeToken(RRBR)
        }

        ok = ok && b.mceEndStmt()
        GdRecovery.topLevel(b)

        return b.exitSection(ok)
    }

}
