package gdscript.parser.roots

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

class GdEnumParser : GdBaseParser {

    constructor(builder: PsiBuilder) : super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(ENUM)) return optional

        val enumDecl = mark()
        advance() // enum

        var ok = true
        if (nextTokenIs(IDENTIFIER)) {
            ok = ok && mceIdentifier(ENUM_DECL_NMI)
        }

        if (ok && nextTokenIs(LCBR)) advance()

        while (ok && nextTokenIs(IDENTIFIER)) {
            ok = enumValue()
            if (!consumeToken(COMMA)) break
        }

        ok && consumeToken(RCBR, true)
        ok && mceEndStmt()
        GdRecovery.topLevel()

        enumDecl.done(ENUM_DECL_TL)

        return true
    }

    private fun enumValue(): Boolean {
        val enumValue = mark()
        mceIdentifier(ENUM_VALUE_NMI)

        var ok = true
        if (consumeToken(EQ)) {
            consumeToken(PLUS)
            consumeToken(MINUS)
            ok = consumeToken(NUMBER, true)
        }
        enumValue.done(ENUM_VALUE)

        return ok
    }

}
