package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.nextTokenIs

class GdFlowStmtParser : GdStmtBaseParser() {

    override val STMT_TYPE: IElementType = FLOW_ST
    override val endWithEndStmt: Boolean = true

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        val flows = arrayOf(CONTINUE, BREAK, PASS, BREAKPOINT)

        if (b.nextTokenIs(*flows)) {
            b.advanceLexer()
        } else if (b.nextTokenIs(RETURN)) {
            b.advanceLexer() // RETURN
            GdExprParser.INSTANCE.parse(b, true)
        } else {
            return false
        }

        return true
    }

}
