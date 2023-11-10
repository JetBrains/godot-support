package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*

class GdAssignStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = ASSIGN_ST
    override val endWithEndStmt: Boolean = true

    constructor(builder: PsiBuilder) : super(builder)

    override fun parse(optional: Boolean): Boolean {
        var ok = GdExprParser.INSTANCE.parse()
        ok = ok && mcAnyOf(ASSIGN_SIGN, EQ, ASSIGN)
        ok = ok && GdExprParser.INSTANCE.parse()

        return ok
    }

}
