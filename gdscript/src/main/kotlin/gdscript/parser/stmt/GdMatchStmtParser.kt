package gdscript.parser.stmt

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

object GdMatchStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = MATCH_ST
    override val endWithEndStmt: Boolean = false

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "MatchStmt")) return false
        if (!b.nextTokenIs(MATCH)) return optional

        var ok = b.consumeToken(MATCH, pin = true)
        ok = ok && GdExprParser.parse(b, l + 1, false)
        b.errorPin(ok, "expression")
        ok = ok && b.consumeToken(COLON)
        ok = ok && b.consumeToken(NEW_LINE)
        ok = ok && b.consumeToken(INDENT)
        ok = ok && matchBlock(b, l + 1)
        while (ok && matchBlock(b, l + 1)) {
        }
        ok = ok && b.consumeToken(DEDENT)

        return ok
    }

    private fun matchBlock(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "MatchBlock")) return false
        b.enterSection(MATCH_BLOCK)
        var ok = true

        ok = ok && patternList(b, l + 1)
        ok = ok && b.consumeToken(COLON, pin = true)
        ok = ok && GdStmtParser.parse(b, l + 1)
        GdRecovery.stmtNoLine(b, ok)

        return b.exitSection(ok, true)
    }

    private fun patternList(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "PatternList")) return false
        val list = b.mark()
        val ok = pattern(b, l + 1, false)
        while (ok && b.nextTokenIs(COMMA))
            pattern(b, l + 1, false)

        if (ok) list.done(PATTERN_LIST)
        else list.drop()

        return ok
    }

    private fun pattern(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "Pattern")) return false
        val pattern = b.mark()

        val ok = b.passToken(UNDER)
            // binding pattern
            || (b.followingTokensAre(VAR, IDENTIFIER) && b.passToken(VAR) && b.mceIdentifier(VAR_NMI))
            || arrayPattern(b, l + 1)
            || dictPattern(b, l + 1)
            || GdExprParser.parse(b, l + 1)
            || optional

        if (ok) pattern.done(PATTERN)
        else pattern.rollbackTo()

        return ok
    }

    private fun arrayPattern(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "ArrayPattern")) return false
        var ok = true
        ok = ok && b.passToken(LSBR)
        ok = ok && pattern(b, l + 1, true)
        while (ok && b.passToken(COMMA)) {
            if (b.passToken(DOTDOT)) break
            ok = ok && pattern(b, l + 1, false)
        }
        ok = ok && b.consumeToken(RSBR)

        return ok
    }

    private fun dictPattern(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "DictPattern")) return false
        val dict = b.mark()
        var ok = true
        ok = ok && b.consumeToken(LCBR)
        ok = ok && keyValuePattern(b, l + 1)
        while (ok && b.nextTokenIs(COMMA)) {
            b.advance()
            if (b.passToken(DOTDOT)) break
            ok = ok && keyValuePattern(b, l + 1)
        }

        ok = ok && b.consumeToken(RCBR)

        if (ok) dict.done(DICT_PATTERN)
        else dict.rollbackTo()

        return ok
    }

    private fun keyValuePattern(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "KeyValuePattern")) return false
        val pattern = b.mark()
        var ok = true
        ok = ok && b.consumeToken(STRING)
        ok = ok && b.consumeToken(COLON)
        ok = ok && pattern(b, l + 1, false)

        if (ok) pattern.done(KEY_VALUE_PATTERN)
        else pattern.rollbackTo()

        return ok
    }

}
