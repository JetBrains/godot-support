package gdscript.parser.roots

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.common.GdParamListParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.mceEndStmt
import gdscript.utils.PsiBuilderUtil.mceIdentifier
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdSignalParser : GdBaseParser {

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(SIGNAL)) return optional

        val signalDecl = b.mark()
        b.advanceLexer() // signal
        var ok = true
        if (b.nextTokenIs(IDENTIFIER)) b.mceIdentifier(SIGNAL_ID_NMI)

        if (ok && b.consumeToken(LRBR)) {
            ok = ok && GdParamListParser.parse(b, false)
            ok = ok && b.consumeToken(RRBR, true)
        }

        ok && b.mceEndStmt()

        GdRecovery.topLevel(b)
        signalDecl.done(SIGNAL_DECL_TL)

        return true
    }

}
