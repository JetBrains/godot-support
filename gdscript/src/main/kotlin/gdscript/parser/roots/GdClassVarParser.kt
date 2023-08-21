package gdscript.parser.roots

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser
import gdscript.parser.common.GdTypedParser
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

class GdClassVarParser : GdBaseParser {

    constructor(builder: PsiBuilder): super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(VAR)) return optional

        val m = mark()
        advance() // const
        var ok = mceIdentifier(VAR_NMI)
        ok = ok && GdTypedParser.INSTANCE.parse(true)

        if (ok && nextTokenIs(EQ, CEQ)) {
            ok = ok && mcAnyOf(ASSIGN_TYPED, EQ, CEQ)
            ok = ok && GdExprParser.INSTANCE.parse()
        }

        ok && (parseGetSet() || mceEndStmt())

        GdRecovery.topLevel()
        m.done(CLASS_VAR_DECL_TL)

        return true
    }

    private fun parseGetSet(): Boolean {
        if (!nextTokenIs(COLON)) return false

        val setGet = mark()
        var ok = consumeToken(COLON)
        var indented = false

        if (ok && nextTokenIs(NEW_LINE)) {
            advance()
            consumeToken(INDENT)
            indented = true
        }

        while (ok) {
            ok = parseGet() || parseSet()
            ok = ok && consumeToken(COMMA)
        }

        mceEndStmt()
        consumeNewLine()
        if (indented) {
            consumeToken(DEDENT, true)
        }
        setGet.done(SETGET_DECL)

        return true
    }

    private fun parseGet(): Boolean {
        if (!nextTokenIs(GET)) return false

        val getDecl = mark()
        var ok = consumeToken(GET)
        ok = ok && (parseMethodVersion(GET_METHOD_ID_NM))
        GdRecovery.setGet()
        getDecl.done(GET_DECL)

        return ok
    }

    private fun parseSet(): Boolean {
        if (!nextTokenIs(SET)) return false

        val setDecl = mark()
        var ok = consumeToken(SET)
        ok = ok && (parseMethodVersion(SET_METHOD_ID_NM))
        GdRecovery.setGet()
        setDecl.done(SET_DECL)

        return ok
    }

    private fun parseMethodVersion(markerType: IElementType): Boolean {
        var ok = consumeToken(EQ)
        ok = ok && mceIdentifier(markerType)

        return ok
    }

}
