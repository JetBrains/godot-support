package gdscript.parser.roots

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdTypedParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

object GdClassConstParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(CONST)) return optional

        b.enterSection(CONST_DECL_TL)
        var ok = b.consumeToken(CONST, pin = true)

        ok = ok && b.mceIdentifier(VAR_NMI)
        ok = ok && GdTypedParser.parseWithAssignTypedAndExpr(b, true)

        ok && b.mceEndStmt()

        GdRecovery.topLevel(b)
        b.exitSection(ok)

        return true
    }

}
