package gdscript.parser.stmt

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*

object GdAssignStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = ASSIGN_ST
    override val endWithEndStmt: Boolean = true

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "AssignStmt")) return false
        var ok = GdExprParser.parse(b, l + 1)
        ok = ok && b.mcToken(ASSIGN_SIGN, EQ, ASSIGN)
        b.pin(ok)
        ok = ok && GdExprParser.parse(b, l + 1)
        b.errorPin(ok, "Expression")

        return ok
    }

}
