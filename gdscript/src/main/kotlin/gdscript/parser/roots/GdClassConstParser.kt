package gdscript.parser.roots

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.common.GdTypedParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

class GdClassConstParser : GdBaseParser {

    constructor(builder: PsiBuilder): super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(CONST)) return optional

        val m = mark()
        advance() // const
        var ok = mceIdentifier(VAR_NMI)
        ok = ok && GdTypedParser.INSTANCE.parseWithAssignTyped(true)

        ok && mceEndStmt()

        GdRecovery.topLevel()
        m.done(CONST_DECL_TL)

        return true
    }

}
