package gdscript.parser.stmt

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdTypedParser
import gdscript.psi.GdTypes.*

object GdConstStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = CONST_DECL_ST
    override val endWithEndStmt: Boolean = true

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(CONST)) return optional

        var ok = b.consumeToken(CONST, pin = true)
        ok = ok && b.mceIdentifier(VAR_NMI)
        ok = ok && GdTypedParser.parseWithAssignTypedAndExpr(b, false)
        b.errorPin(ok, "Expression")

        return ok || b.pinned()
    }

}
