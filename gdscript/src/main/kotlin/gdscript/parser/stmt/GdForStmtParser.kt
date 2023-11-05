package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

class GdForStmtParser : GdBaseParser {

    constructor(builder: PsiBuilder) : super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(FOR)) return false

        val stmt = mark()
        advance() // for
        var ok = true
        ok = ok && mceIdentifier(VAR_NMI)
        ok = ok && consumeToken(IN, true)
        ok = ok && GdExprParser.INSTANCE.parse()
        ok = ok && consumeToken(COLON, true)
        ok = ok && GdStmtParser.INSTANCE.parse()

        GdRecovery.stmt()
        stmt.done(FOR_ST)

        return ok
    }

}
