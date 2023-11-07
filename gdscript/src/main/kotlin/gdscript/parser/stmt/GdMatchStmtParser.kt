package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

class GdMatchStmtParser : GdBaseParser {

    constructor(builder: PsiBuilder) : super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(MATCH)) return optional

        val stmt = mark()
        advance() // match
        var ok = true
        ok = ok && GdExprParser.INSTANCE.parse(false)
        ok = ok && consumeToken(COLON, true)
        ok = ok && consumeToken(NEW_LINE, true)
        ok = ok && consumeToken(INDENT, true)
        ok = ok && matchBlock()
        while (matchBlock()) {
        }
        ok = ok && consumeToken(DEDENT, true)

        GdRecovery.stmt()
        stmt.done(MATCH_ST)

        return ok
    }

    private fun matchBlock(): Boolean {
        val block = mark()
        var ok = true
        var pin = false

        ok = ok && patternList()
        ok = ok && consumeToken(COLON)
        pin = ok
        ok = ok && GdStmtParser.INSTANCE.parse()
        if (pin) {
            GdRecovery.stmtNoLine()
            block.done(MATCH_BLOCK)
        } else {
            block.rollbackTo()
        }

        return pin
    }

    private fun patternList(): Boolean {
        val list = mark()
        val ok = pattern(false)
        while (ok && nextTokenIs(COMMA))
            pattern(false)
        if (ok) list.done(PATTERN_LIST)
        else list.drop()

        return ok
    }

    private fun pattern(optional: Boolean): Boolean {
        val pattern = mark()

        val ok = consumeToken(UNDER)
            // binding pattern
            || (followingTokensAre(VAR, IDENTIFIER) && consumeToken(VAR) && mceIdentifier(VAR_NMI))
            || arrayPattern()
            || dictPattern()
            || GdExprParser.INSTANCE.parse()
            || optional

        if (ok) pattern.done(PATTERN)
        else pattern.rollbackTo()

        return ok
    }

    private fun arrayPattern(): Boolean {
        var ok = true
        ok = ok && consumeToken(LSBR)
        ok = ok && pattern(true)
        while (ok && nextTokenIs(COMMA)) {
            advance() // comma
            if (consumeToken(DOTDOT)) break
            ok = ok && pattern(false)
        }
        ok = ok && consumeToken(RSBR)

        return ok
    }

    private fun dictPattern(): Boolean {
        val dict = mark()
        var ok = true
        ok = ok && consumeToken(LCBR)
        ok = ok && keyValuePattern()
        while (ok && nextTokenIs(COMMA)) {
            advance()
            if (consumeToken(DOTDOT)) break
            ok = ok && keyValuePattern()
        }

        ok = ok && consumeToken(RCBR)

        if (ok) dict.done(DICT_PATTERN)
        else dict.rollbackTo()

        return ok
    }

    private fun keyValuePattern(): Boolean {
        val pattern = mark()
        var ok = true
        ok = ok && consumeToken(STRING)
        ok = ok && consumeToken(COLON)
        ok = ok && pattern(false)

        if (ok) pattern.done(KEY_VALUE_PATTERN)
        else pattern.rollbackTo()

        return ok
    }

}
