package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdFlowStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = FLOW_ST
    override val endWithEndStmt: Boolean = true
    override val pinnable: Boolean = false

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        val flows = arrayOf(CONTINUE, BREAK, PASS, BREAKPOINT)

        if (b.nextTokenIs(*flows)) {
            b.advanceLexer()
        } else if (b.nextTokenIs(RETURN)) {
            b.advanceLexer() // RETURN
            GdExprParser.parse(b, true)
        } else {
            return false
        }

        return true
    }

}
