package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.EXPR_ST

class GdExStmtParser : GdBaseParser {

    constructor(builder: PsiBuilder) : super(builder)

    override fun parse(optional: Boolean): Boolean {
        val stmt = mark()

        var ok = true
        ok = ok && GdExprParser.INSTANCE.parse()
        ok = ok && mceEndStmt()

        if (ok) stmt.done(EXPR_ST)
        else stmt.rollbackTo()

        return ok
    }

}
