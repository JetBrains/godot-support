package gdscript.parser.roots

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdTypedParser
import gdscript.parser.recovery.GdRecovery
import gdscript.parser.stmt.GdStmtParser
import gdscript.psi.GdTypes.*

object GdClassVarParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(VAR, STATIC)) return optional

        b.enterSection(CLASS_VAR_DECL_TL)
        if (b.passToken(STATIC)) b.pin(true)
        var ok = b.consumeToken(VAR, pin = true)
        ok = ok && b.mceIdentifier(VAR_NMI)

        ok = ok && GdTypedParser.parseWithAssignTypedAndExpr(b, true)
        ok = ok && (parseGetSet(b) || b.mceEndStmt())

        GdRecovery.topLevel(b, ok)

        return b.exitSection(ok)
    }

    private fun parseGetSet(b: GdPsiBuilder): Boolean {
        if (!b.nextTokenIs(COLON)) return false

        b.enterSection(SETGET_DECL)
        var ok = b.consumeToken(COLON, pin = true)
        var indented = false

        if (ok && b.nextTokenIs(NEW_LINE)) {
            b.advance()
            ok = ok && b.consumeToken(INDENT)
            indented = true
        }

        while (ok) {
            ok = parseGet(b) || parseSet(b)
            b.passToken(COMMA) && b.mceEndStmt(true)
        }

        b.mceEndStmt(true)
        indented && b.consumeToken(DEDENT)

        ok = b.exitSection(true)

        return ok
    }

    private fun parseGet(b: GdPsiBuilder): Boolean {
        if (!b.nextTokenIs(GET)) return false

        b.enterSection(GET_DECL)
        var ok = b.consumeToken(GET, pin = true)

        ok = ok && (parseMethodVersion(b, GET_METHOD_ID_NM) || parseStmtVersion(b, GET))
        GdRecovery.setGet(b)

        return b.exitSection(ok)
    }

    private fun parseSet(b: GdPsiBuilder): Boolean {
        if (!b.nextTokenIs(SET)) return false

        b.enterSection(SET_DECL)
        var ok = b.consumeToken(SET, pin = true)
        ok = ok && (parseMethodVersion(b, SET_METHOD_ID_NM) || parseStmtVersion(b, SET))
        GdRecovery.setGet(b)

        return b.exitSection(ok)
    }

    private fun parseMethodVersion(b: GdPsiBuilder, markerType: IElementType): Boolean {
        var ok = b.passToken(EQ)
        ok = ok && b.mceIdentifier(markerType)

        return ok
    }

    private fun parseStmtVersion(b: GdPsiBuilder, markerType: IElementType): Boolean {
        var ok = true
        if (markerType == SET) {
            ok = ok && b.consumeToken(LRBR)
            ok = ok && b.mceIdentifier(VAR_NMI)
            ok = ok && GdTypedParser.parse(b, true)
            ok = ok && b.consumeToken(RRBR)
        }

        ok = ok && b.consumeToken(COLON)
        ok = ok && GdStmtParser.parse(b, false)

        return ok
    }

}
