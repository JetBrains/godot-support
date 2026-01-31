package gdscript.parser.stmt

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.BREAK
import gdscript.psi.GdTypes.BREAKPOINT
import gdscript.psi.GdTypes.CONTINUE
import gdscript.psi.GdTypes.FLOW_ST
import gdscript.psi.GdTypes.PASS
import gdscript.psi.GdTypes.RETURN

object GdFlowStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = FLOW_ST
    override val endWithEndStmt: Boolean = true

    private val flows = arrayOf(CONTINUE, BREAK, PASS, BREAKPOINT)

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "FlowStmt")) return false

        if (b.passToken(*flows)) {
            b.pin()
            return true
        } else if (b.passToken(RETURN)) {
            b.pin()
            GdExprParser.parse(b, l + 1, true)
            return true
        }

        return false
    }

}
