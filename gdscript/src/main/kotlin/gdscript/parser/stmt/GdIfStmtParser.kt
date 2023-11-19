package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdIfStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = IF_ST
    override val endWithEndStmt: Boolean = false

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(IF)) return optional

        b.advanceLexer() // if
        var ok = true
        ok = ok && GdExprParser.parse(b)
        ok = ok && b.consumeToken(COLON, true)
        ok = ok && GdStmtParser.parse(b)

        while (ok && elifSt(b)) {}
        ok && elseSt(b)

        return ok
    }

    private fun elifSt(b: PsiBuilder): Boolean {
        if (!b.nextTokenIs(ELIF)) return false

        val elif = b.mark()
        var ok = true
        b.advanceLexer() // elif
        ok = ok && GdExprParser.parse(b)
        ok = ok && b.consumeToken(COLON, true)
        ok = ok && GdStmtParser.parse(b)

        GdRecovery.stmt(b)
        elif.done(ELIF_ST)

        return true
    }

    private fun elseSt(b: PsiBuilder): Boolean {
        if (!b.nextTokenIs(ELSE)) return false

        val elseSt = b.mark()
        var ok = true
        b.advanceLexer() // else
        ok = ok && b.consumeToken(COLON, true)
        ok = ok && GdStmtParser.parse(b)

        GdRecovery.stmt(b)
        elseSt.done(ELSE_ST)

        return true
    }

}
