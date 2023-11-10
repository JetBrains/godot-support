package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.common.GdTypedParser
import gdscript.psi.GdTypes.*

class GdVarStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = VAR_DECL_ST
    override val endWithEndStmt: Boolean = true

    constructor(builder: PsiBuilder) : super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(VAR)) return false

        advance() // var
        var ok = true
        ok = ok && mceIdentifier(VAR_NMI)
        ok = ok && GdTypedParser.INSTANCE.parseWithAssignTypedAndExpr(true)

        return ok
    }

}
