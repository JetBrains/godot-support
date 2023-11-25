package gdscript.parser.roots

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.common.GdParamListParser
import gdscript.parser.common.GdReturnHintParser
import gdscript.parser.recovery.GdRecovery
import gdscript.parser.stmt.GdStmtParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.mceIdentifier
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdMethodParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        val methodDecl = b.mark()
        var ok = markers(b)
        if (!ok && !b.nextTokenIs(FUNC)) {
            methodDecl.rollbackTo()
            return optional
        }
        ok = b.consumeToken(FUNC, true)
        ok = ok && b.mceIdentifier(METHOD_ID_NMI)
        ok = ok && b.consumeToken(LRBR)

        ok = ok && GdParamListParser.parse(b, true)

        ok = ok && b.consumeToken(RRBR, true)
        ok = ok && GdReturnHintParser.parse(b, true)
        ok = ok && b.consumeToken(COLON, true)
        ok = ok && GdStmtParser.parse(b)

        GdRecovery.topLevel(b)
        methodDecl.done(METHOD_DECL_TL)

        return true
    }

    private fun markers(b: GdPsiBuilder): Boolean {
        var marked = false
        marked = marked || b.consumeToken(REMOTE)
        marked = marked || b.consumeToken(MASTER)
        marked = marked || b.consumeToken(PUPPET)
        marked = marked || b.consumeToken(REMOTESYNC)
        marked = marked || b.consumeToken(MASTERSYNC)
        marked = marked || b.consumeToken(PUPPETSYNC)
        marked = b.consumeToken(STATIC) || marked

        return marked
    }

}
