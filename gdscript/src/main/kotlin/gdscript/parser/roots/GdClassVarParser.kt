package gdscript.parser.roots

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdTypedParser
import gdscript.parser.recovery.GdRecovery
import gdscript.parser.stmt.GdStmtParser
import gdscript.psi.GdTypes.*

object GdClassVarParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "ClassVar")) return false
        if (!b.nextTokenIs(VAR, STATIC)) return optional

        b.enterSection(CLASS_VAR_DECL_TL)
        if (b.passToken(STATIC)) {
            // Separate check due to static being optional before FUNC as well
            if (b.nextTokenIs(NEW_LINE)) {
                b.pin(true)
                b.error(VAR.toString(), false)
                GdRecovery.topLevel(b, false)
                return b.exitSection(false)
            }
        }
        var ok = b.consumeToken(VAR, pin = true)
        ok = ok && b.mceIdentifier(VAR_NMI)

        ok = ok && GdTypedParser.parseWithAssignTypedAndExpr(b, l + 1, true)
        ok = ok && (parseGetSet(b, l + 1) || b.mceEndStmt())

        GdRecovery.topLevel(b, ok)

        return b.exitSection(ok, !ok && !b.pinned())
    }

    private fun parseGetSet(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "GetSet")) return false
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
            ok = parseGet(b, l + 1) || parseSet(b, l + 1)
            b.passToken(COMMA) && b.mceEndStmt(true)
        }

        b.mceEndStmt(true)
        indented && b.consumeToken(DEDENT)

        ok = b.exitSection(true)

        return ok
    }

    private fun parseGet(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "Get")) return false
        if (!b.nextTokenIs(GET)) return false

        b.enterSection(GET_DECL)
        var ok = b.consumeToken(GET, pin = true)

        ok = ok && (parseMethodVersion(b, l + 1, GET_METHOD_ID_NM) || parseStmtVersion(b, l + 1, GET))
        GdRecovery.setGet(b)

        return b.exitSection(ok)
    }

    private fun parseSet(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "Set")) return false
        if (!b.nextTokenIs(SET)) return false

        b.enterSection(SET_DECL)
        var ok = b.consumeToken(SET, pin = true)
        ok = ok && (parseMethodVersion(b, l + 1, SET_METHOD_ID_NM) || parseStmtVersion(b, l + 1, SET))
        GdRecovery.setGet(b)

        return b.exitSection(ok)
    }

    private fun parseMethodVersion(b: GdPsiBuilder, l: Int, markerType: IElementType): Boolean {
        if (!b.recursionGuard(l, "MethodGetSet")) return false
        var ok = b.passToken(EQ)
        ok = ok && b.mceIdentifier(markerType)

        return ok
    }

    private fun parseStmtVersion(b: GdPsiBuilder, l: Int, markerType: IElementType): Boolean {
        if (!b.recursionGuard(l, "StmtGetSet")) return false
        var ok = true
        if (markerType == SET) {
            ok = ok && b.consumeToken(LRBR)
            ok = ok && b.mceIdentifier(VAR_NMI)
            ok = ok && GdTypedParser.parse(b, l + 1, true)
            ok = ok && b.consumeToken(RRBR)
        }

        ok = ok && b.consumeToken(COLON)
        ok = ok && GdStmtParser.parse(b, l + 1, false)

        return ok
    }

}
