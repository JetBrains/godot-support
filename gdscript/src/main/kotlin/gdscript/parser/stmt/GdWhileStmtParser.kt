package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdWhileStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = WHILE_ST
    override val endWithEndStmt: Boolean = false
    override val pinnable: Boolean = false

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(WHILE)) return false

        b.advanceLexer() // while
        var ok = true
        ok = ok && GdExprParser.parse(b)
        ok = ok && b.consumeToken(COLON)

        ok = ok && GdStmtParser.parse(b)

        return ok
    }

}
