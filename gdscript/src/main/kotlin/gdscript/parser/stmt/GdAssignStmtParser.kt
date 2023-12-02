package gdscript.parser.stmt

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*

object GdAssignStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = ASSIGN_ST
    override val endWithEndStmt: Boolean = true
    override val pinnable: Boolean = false

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        var ok = GdExprParser.parse(b)
        ok = ok && b.mcToken(ASSIGN_SIGN, EQ, ASSIGN)
        b.pin(ok)
        ok = ok && GdExprParser.parse(b)
        b.errorPin(ok, "Expression")

        return ok
    }

}
