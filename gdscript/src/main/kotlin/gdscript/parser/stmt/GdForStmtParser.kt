package gdscript.parser.stmt

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdTypedParser
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.COLON
import gdscript.psi.GdTypes.FOR
import gdscript.psi.GdTypes.FOR_ST
import gdscript.psi.GdTypes.IN
import gdscript.psi.GdTypes.VAR_NMI

object GdForStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = FOR_ST
    override val endWithEndStmt: Boolean = false

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "ForStmt")) return false
        if (!b.nextTokenIs(FOR)) return false

        var ok = b.consumeToken(FOR, pin = true)
        ok = ok && b.mceIdentifier(VAR_NMI)
        ok = ok && GdTypedParser.parse(b, l + 1, true)
        ok = ok && b.consumeToken(IN, true)
        ok = ok && GdExprParser.parse(b, l + 1)
        b.errorPin(ok, "expression")
        ok = ok && b.consumeToken(COLON, true)
        ok = ok && GdStmtParser.parse(b, l + 1)
        b.errorPin(ok, "statement")

        return ok
    }

}
