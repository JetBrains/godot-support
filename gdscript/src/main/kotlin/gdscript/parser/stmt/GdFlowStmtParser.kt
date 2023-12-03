package gdscript.parser.stmt

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*

object GdFlowStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = FLOW_ST
    override val endWithEndStmt: Boolean = true

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        val flows = arrayOf(CONTINUE, BREAK, PASS, BREAKPOINT)

        if (b.passToken(*flows)) {
            return true
        } else if (b.passToken(RETURN)) {
            GdExprParser.parse(b, true)
            return true
        }

        return false
    }

}
