package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*

class GdFlowStmtParser : GdBaseParser {

    constructor(builder: PsiBuilder) : super(builder)

    override fun parse(optional: Boolean): Boolean {
        val stmt = mark()
        val flows = arrayOf(CONTINUE, BREAK, PASS, BREAKPOINT)

        if (nextTokenIs(*flows)) {
            advance()
        } else if (nextTokenIs(RETURN)) {
            advance() // RETURN
            GdExprParser.INSTANCE.parse(true)
        } else {
            stmt.rollbackTo()
            return false
        }

        mceEndStmt()
        stmt.done(FLOW_ST)

        return true
    }

}
