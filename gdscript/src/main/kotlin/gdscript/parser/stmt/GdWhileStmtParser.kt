package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*

class GdWhileStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = WHILE_ST

    constructor(builder: PsiBuilder) : super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(WHILE)) return false

        advance() // while
        var ok = true
        ok = ok && GdExprParser.INSTANCE.parse()
        ok = ok && consumeToken(COLON)

        ok = ok && GdStmtParser.INSTANCE.parse()

        return ok
    }

}
