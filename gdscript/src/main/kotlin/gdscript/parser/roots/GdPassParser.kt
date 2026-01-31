package gdscript.parser.roots

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.DEDENT
import gdscript.psi.GdTypes.FLOW_ST
import gdscript.psi.GdTypes.PASS

object GdPassParser : GdBaseParser {

    // there is also GdFlowStmtParser, which also handles pass statements

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.nextTokenIs(PASS)) return false
        b.enterSection(FLOW_ST)
        var ok = b.consumeToken(PASS, pin = true)
        // Allow either an explicit end-of-statement (semicolon/newline) or immediate DEDENT (EOF in suite)
        if (!b.nextTokenIs(DEDENT)) {
            ok = ok && b.mceEndStmt()
        }
        // Recover to the next statement boundary
        if (ok) GdRecovery.stmt(b) else GdRecovery.stmtNoLine(b)
        b.exitSection(ok, true)
        return ok
    }
}
