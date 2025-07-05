package gdscript.parser.roots

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

object GdEnumParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "Enum")) return false
        if (!b.nextTokenIs(ENUM)) return optional

        b.enterSection(ENUM_DECL_TL)
        var ok = b.consumeToken(ENUM, pin = true)

        if (b.nextTokenIs(IDENTIFIER)) {
            ok = ok && b.mceIdentifier(ENUM_DECL_NMI)
        }

        val lcbr = ok && b.passToken(LCBR)
        while (ok && b.nextTokenIs(IDENTIFIER)) {
            ok = enumValue(b, l + 1)
            if (!b.passToken(COMMA)) break
        }

        ok = ok && lcbr && b.consumeToken(RCBR)
        ok = ok && b.mceEndStmt()

        GdRecovery.topLevel(b)

        return b.exitSection(ok)
    }

    private fun enumValue(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "EnumValue")) return false
        b.enterSection(ENUM_VALUE)
        b.mceIdentifier(ENUM_VALUE_NMI)

        var ok = true
        if (b.passToken(EQ)) {
            b.passToken(PLUS, MINUS)
            ok = ok && b.consumeToken(NUMBER)
        }

        return b.exitSection(ok)
    }

}
