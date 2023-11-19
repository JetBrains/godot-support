package gdscript.parser.roots

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser
import gdscript.parser.common.GdTypedParser
import gdscript.parser.recovery.GdRecovery
import gdscript.parser.stmt.GdStmtParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.mceEndStmt
import gdscript.utils.PsiBuilderUtil.mceIdentifier
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdClassVarParser : GdBaseParser {

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(VAR, STATIC)) return optional

        val m = b.mark()
        b.consumeToken(STATIC)
        b.advanceLexer() // var
        var ok = b.mceIdentifier(VAR_NMI)

        ok = ok && GdTypedParser.parseWithAssignTypedAndExpr(b, true)
        ok = ok && (parseGetSet(b) || b.mceEndStmt())

        GdRecovery.topLevel(b)
        m.done(CLASS_VAR_DECL_TL)

        return true
    }

    private fun parseGetSet(b: PsiBuilder): Boolean {
        if (!b.nextTokenIs(COLON)) return false

        val setGet = b.mark()
        var ok = b.consumeToken(COLON)
        var indented = false

        if (ok && b.nextTokenIs(NEW_LINE)) {
            b.advanceLexer()
            ok = ok && b.consumeToken(INDENT)
            indented = true
        }

        while (ok) {
            ok = parseGet(b) || parseSet(b)
            b.consumeToken(COMMA) && b.mceEndStmt(false)
        }

        b.mceEndStmt(false)
        if (indented) {
            b.consumeToken(DEDENT, true)
        }
        setGet.done(SETGET_DECL)

        return true
    }

    private fun parseGet(b: PsiBuilder): Boolean {
        if (!b.nextTokenIs(GET)) return false

        val getDecl = b.mark()
        var ok = b.consumeToken(GET)
        ok = ok && (parseMethodVersion(b, GET_METHOD_ID_NM) || parseStmtVersion(b, GET))
        GdRecovery.setGet(b)
        getDecl.done(GET_DECL)

        return ok
    }

    private fun parseSet(b: PsiBuilder): Boolean {
        if (!b.nextTokenIs(SET)) return false

        val setDecl = b.mark()
        var ok = b.consumeToken(SET)
        ok = ok && (parseMethodVersion(b, SET_METHOD_ID_NM) || parseStmtVersion(b, SET))
        GdRecovery.setGet(b)
        setDecl.done(SET_DECL)

        return ok
    }

    private fun parseMethodVersion(b: PsiBuilder, markerType: IElementType): Boolean {
        var ok = b.consumeToken(EQ)
        ok = ok && b.mceIdentifier(markerType)

        return ok
    }

    private fun parseStmtVersion(b: PsiBuilder, markerType: IElementType): Boolean {
        var ok = true
        if (markerType == SET) {
            ok = ok && b.consumeToken(LRBR, true)
            ok = ok && b.mceIdentifier(VAR_NMI)
            ok = ok && GdTypedParser.parse(b, true)
            ok = ok && b.consumeToken(RRBR, true)
        }

        ok = ok && b.consumeToken(COLON, true)
        ok = ok && GdStmtParser.parse(b, false)

        return ok
    }

}
