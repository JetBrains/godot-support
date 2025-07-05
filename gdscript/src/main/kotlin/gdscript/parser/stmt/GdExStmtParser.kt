package gdscript.parser.stmt

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.EXPR_ST

object GdExStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = EXPR_ST
    override val endWithEndStmt: Boolean = true

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "ExStmt")) return false
        var ok = true
        ok = ok && GdExprParser.parse(b, l + 1)
        b.pin(ok)

        return ok
    }

}
