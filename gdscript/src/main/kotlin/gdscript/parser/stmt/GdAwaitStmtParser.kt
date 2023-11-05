package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.AWAIT
import gdscript.psi.GdTypes.AWAIT_ST

class GdAwaitStmtParser : GdBaseParser {

    constructor(builder: PsiBuilder) : super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(AWAIT)) return false

        val stmt = mark()
        advance() // await
        var ok = true
        ok = ok && GdExprParser.INSTANCE.parse()
        ok = ok && mceEndStmt()

        GdRecovery.stmt()
        stmt.done(AWAIT_ST)

        return ok
    }

}
