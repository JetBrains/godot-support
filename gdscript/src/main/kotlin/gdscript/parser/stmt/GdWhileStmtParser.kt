package gdscript.parser.stmt

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.COLON
import gdscript.psi.GdTypes.WHILE
import gdscript.psi.GdTypes.WHILE_ST

object GdWhileStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = WHILE_ST
    override val endWithEndStmt: Boolean = false

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.nextTokenIs(WHILE)) return false

        var ok = b.consumeToken(WHILE, pin = true)
        ok = ok && GdExprParser.parse(b, l + 1)
        b.errorPin(ok, "expression")
        ok = ok && b.consumeToken(COLON)

        ok = ok && GdStmtParser.parse(b, l + 1)
        b.errorPin(ok, "statement")

        return ok
    }

}
