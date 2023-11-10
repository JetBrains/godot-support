package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*

class GdFlowStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = FLOW_ST
    override val endWithEndStmt: Boolean = true

    constructor(builder: PsiBuilder) : super(builder)

    override fun parse(optional: Boolean): Boolean {
        val flows = arrayOf(CONTINUE, BREAK, PASS, BREAKPOINT)

        if (nextTokenIs(*flows)) {
            advance()
        } else if (nextTokenIs(RETURN)) {
            advance() // RETURN
            GdExprParser.INSTANCE.parse(true)
        } else {
            return false
        }

        return true
    }

}
