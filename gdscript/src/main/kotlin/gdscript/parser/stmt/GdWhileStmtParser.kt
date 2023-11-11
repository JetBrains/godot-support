package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.nextTokenIs

class GdWhileStmtParser : GdStmtBaseParser() {

    override val STMT_TYPE: IElementType = WHILE_ST

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(WHILE)) return false

        b.advanceLexer() // while
        var ok = true
        ok = ok && GdExprParser.INSTANCE.parse(b)
        ok = ok && b.consumeToken(COLON)

        ok = ok && GdStmtParser.INSTANCE.parse(b)

        return ok
    }

}
