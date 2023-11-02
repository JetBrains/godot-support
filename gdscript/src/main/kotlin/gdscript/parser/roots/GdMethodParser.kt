package gdscript.parser.roots

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.common.GdParamListParser
import gdscript.parser.common.GdTypedParser
import gdscript.parser.recovery.GdRecovery
import gdscript.parser.stmt.GdStmtParser
import gdscript.psi.GdTypes.*

class GdMethodParser : GdBaseParser {

    constructor(builder: PsiBuilder) : super(builder)

    override fun parse(optional: Boolean): Boolean {
        val methodDecl = mark()
        var ok = markers()
        if (!ok && !nextTokenIs(FUNC)) {
            methodDecl.drop()
            return optional
        }
        ok = consumeToken(FUNC, true)
        ok = ok && mceIdentifier(METHOD_ID_NMI)
        ok = ok && consumeToken(LRBR)

        ok = ok && GdParamListParser.INSTANCE.parse(true)

        ok = ok && consumeToken(RRBR, true)
        ok = ok && returnHint()
        ok = ok && consumeToken(COLON, true)
        ok = ok && GdStmtParser.INSTANCE.parse()

        GdRecovery.topLevel()
        methodDecl.done(METHOD_DECL_TL)

        return true
    }

    private fun markers(): Boolean {
        var marked = false
        marked = marked || consumeToken(REMOTE)
        marked = marked || consumeToken(MASTER)
        marked = marked || consumeToken(PUPPET)
        marked = marked || consumeToken(REMOTESYNC)
        marked = marked || consumeToken(MASTERSYNC)
        marked = marked || consumeToken(PUPPETSYNC)
        marked = consumeToken(STATIC) || marked

        return marked
    }

    private fun returnHint(): Boolean {
        if (!nextTokenIs(RET)) return true
        val hint = mark()
        var ok = true
        advance() // RET

        val hintVal = mark()
        if (nextTokenIs(VOID)) {
            advance() // VOID
        } else {
            ok = ok && GdTypedParser.INSTANCE.typedVal(false)
        }

        hintVal.done(RETURN_HINT_VAL)
        hint.done(RETURN_HINT)

        return ok
    }

}
