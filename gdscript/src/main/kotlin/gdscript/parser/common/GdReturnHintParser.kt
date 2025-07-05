package gdscript.parser.common

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

object GdReturnHintParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "ReturnHint")) return false
        if (!b.nextTokenIs(RET)) return optional

        b.enterSection(RETURN_HINT)
        var ok = b.consumeToken(RET)

        b.enterSection(RETURN_HINT_VAL)
        if (!b.passToken(VOID)) {
            ok = ok && GdTypedParser.typedVal(b, l + 1, false)
        }

        b.exitSection(ok)

        return b.exitSection(ok) || optional
    }

}
