package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

class GdWhileStmtParser : GdBaseParser {

    constructor(builder: PsiBuilder) : super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(WHILE)) return false

        val stmt = mark()
        advance() // while
        var ok = true
        ok = ok && GdExprParser.INSTANCE.parse()
        ok = ok && consumeToken(COLON)

        ok = ok && GdStmtParser.INSTANCE.parse()

        GdRecovery.stmt()
        stmt.done(WHILE_ST)

        return ok
    }

}
