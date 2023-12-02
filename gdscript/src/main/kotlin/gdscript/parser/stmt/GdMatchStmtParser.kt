package gdscript.parser.stmt

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

object GdMatchStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = MATCH_ST
    override val endWithEndStmt: Boolean = false
    override val pinnable: Boolean = false

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(MATCH)) return optional

        var ok = b.consumeToken(MATCH, pin = true)
        ok = ok && GdExprParser.parse(b, false)
        b.errorPin(ok, "expression")
        ok = ok && b.consumeToken(COLON)
        ok = ok && b.consumeToken(NEW_LINE)
        ok = ok && b.consumeToken(INDENT)
        ok = ok && matchBlock(b)
        while (ok && matchBlock(b)) {
        }
        ok = ok && b.consumeToken(DEDENT)

        return ok
    }

    private fun matchBlock(b: GdPsiBuilder): Boolean {
        b.enterSection(MATCH_BLOCK)
        var ok = true

        ok = ok && patternList(b)
        ok = ok && b.consumeToken(COLON, pin = true)
        ok = ok && GdStmtParser.parse(b)
        GdRecovery.stmtNoLine(b, ok)

        return b.exitSection(ok)
    }

    private fun patternList(b: GdPsiBuilder): Boolean {
        val list = b.mark()
        val ok = pattern(b, false)
        while (ok && b.nextTokenIs(COMMA))
            pattern(b, false)

        if (ok) list.done(PATTERN_LIST)
        else list.drop()

        return ok
    }

    private fun pattern(b: GdPsiBuilder, optional: Boolean): Boolean {
        val pattern = b.mark()

        val ok = b.passToken(UNDER)
            // binding pattern
            || (b.followingTokensAre(VAR, IDENTIFIER) && b.passToken(VAR) && b.mceIdentifier(VAR_NMI))
            || arrayPattern(b)
            || dictPattern(b)
            || GdExprParser.parse(b)
            || optional

        if (ok) pattern.done(PATTERN)
        else pattern.rollbackTo()

        return ok
    }

    private fun arrayPattern(b: GdPsiBuilder): Boolean {
        var ok = true
        ok = ok && b.passToken(LSBR)
        ok = ok && pattern(b, true)
        while (ok && b.passToken(COMMA)) {
            if (b.passToken(DOTDOT)) break
            ok = ok && pattern(b, false)
        }
        ok = ok && b.consumeToken(RSBR)

        return ok
    }

    private fun dictPattern(b: GdPsiBuilder): Boolean {
        val dict = b.mark()
        var ok = true
        ok = ok && b.consumeToken(LCBR)
        ok = ok && keyValuePattern(b)
        while (ok && b.nextTokenIs(COMMA)) {
            b.advance()
            if (b.passToken(DOTDOT)) break
            ok = ok && keyValuePattern(b)
        }

        ok = ok && b.consumeToken(RCBR)

        if (ok) dict.done(DICT_PATTERN)
        else dict.rollbackTo()

        return ok
    }

    private fun keyValuePattern(b: GdPsiBuilder): Boolean {
        val pattern = b.mark()
        var ok = true
        ok = ok && b.consumeToken(STRING)
        ok = ok && b.consumeToken(COLON)
        ok = ok && pattern(b, false)

        if (ok) pattern.done(KEY_VALUE_PATTERN)
        else pattern.rollbackTo()

        return ok
    }

}
