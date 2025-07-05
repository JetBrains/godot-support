package gdscript.parser.stmt

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdTypedParser
import gdscript.psi.GdTypes.*

object GdVarStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = VAR_DECL_ST
    override val endWithEndStmt: Boolean = true

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.nextTokenIs(VAR)) return false

        var ok = b.consumeToken(VAR, pin = true)
        ok = ok && b.mceIdentifier(VAR_NMI)
        ok = ok && GdTypedParser.parseWithAssignTypedAndExpr(b, l + 1, true)

        return ok || b.pinned()
    }

}
