package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdParamListParser
import gdscript.parser.common.GdReturnHintParser
import gdscript.parser.recovery.GdRecovery
import gdscript.parser.stmt.GdStmtParser
import gdscript.psi.GdTypes.*

object GdFuncDeclExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = FUNC_DECL_EX

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "FuncDeclExpr")) return false
        if (!b.nextTokenIs(FUNC)) return false

        var ok = b.consumeToken(FUNC, pin = true)
        if (b.nextTokenIs(IDENTIFIER)) b.mceIdentifier(FUNC_DECL_ID_NMI)
        ok = ok && b.consumeToken(LRBR)
        ok = ok && GdParamListParser.parse(b, l + 1, true)
        ok = ok && b.consumeToken(RRBR)
        ok = ok && GdReturnHintParser.parse(b, l + 1, true)
        ok = ok && b.consumeToken(COLON)
        ok = ok && GdStmtParser.parseLambda(b, l + 1, false, true)

        if (ok || b.pinned()) GdRecovery.stmt(b)

        return ok || b.pinned()
    }

}
