package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.mcAnyOf

object GdAssignStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = ASSIGN_ST
    override val endWithEndStmt: Boolean = true

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        var ok = GdExprParser.parse(b)
        ok = ok && b.mcAnyOf(ASSIGN_SIGN, EQ, ASSIGN)
        ok = ok && GdExprParser.parse(b)

        return ok
    }

}
