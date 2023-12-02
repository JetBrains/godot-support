package gdscript.parser.stmt

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

object GdIfStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = IF_ST
    override val endWithEndStmt: Boolean = false
    override val pinnable: Boolean = false

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(IF)) return optional

        var ok = b.consumeToken(IF, pin = true)
        ok = ok && GdExprParser.parse(b)
        b.errorPin(ok, "expression")
        ok = ok && b.consumeToken(COLON, true)
        ok = ok && GdStmtParser.parse(b)
        b.errorPin(ok, "statement")

        while (ok && elifSt(b)) {}
        ok && elseSt(b)

        return ok
    }

    private fun elifSt(b: GdPsiBuilder): Boolean {
        if (!b.nextTokenIs(ELIF)) return false

        b.enterSection(ELIF_ST)
        var ok = b.consumeToken(ELIF, pin = true)
        ok = ok && GdExprParser.parse(b)
        ok = ok && b.consumeToken(COLON, true)
        ok = ok && GdStmtParser.parse(b)

        GdRecovery.stmt(b, ok)

        return b.exitSection(ok)
    }

    private fun elseSt(b: GdPsiBuilder): Boolean {
        if (!b.nextTokenIs(ELSE)) return false

        b.enterSection(ELSE_ST)

        var ok = b.consumeToken(ELSE, pin = true)
        ok = ok && b.consumeToken(COLON, true)
        ok = ok && GdStmtParser.parse(b)

        GdRecovery.stmt(b, ok)

        return b.exitSection(ok)
    }

}
