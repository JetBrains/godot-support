package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.AWAIT
import gdscript.psi.GdTypes.AWAIT_ST

class GdAwaitStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = AWAIT_ST
    override val endWithEndStmt: Boolean = true

    constructor(builder: PsiBuilder) : super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(AWAIT)) return false

        advance() // await
        var ok = true
        ok = ok && GdExprParser.INSTANCE.parse()

        return ok
    }

}
