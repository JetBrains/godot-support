package gdscript.parser.roots

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

class GdSignalParser : GdBaseParser {

    constructor(builder: PsiBuilder): super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(SIGNAL)) return optional

        val signalDecl = mark()
        advance() // signal
        var ok = mceIdentifier(SIGNAL_ID_NMI)

        if (ok && nextTokenIs(LRBR)) {
            advance()

        }

        ok && mceEndStmt()

        GdRecovery.topLevel()
        signalDecl.done(SIGNAL_DECL_TL)

        return true
    }

}
