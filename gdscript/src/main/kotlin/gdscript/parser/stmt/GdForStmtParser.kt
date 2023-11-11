package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.mceIdentifier
import gdscript.utils.PsiBuilderUtil.nextTokenIs

class GdForStmtParser : GdStmtBaseParser() {

    override val STMT_TYPE: IElementType = FOR_ST

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(FOR)) return false

        b.advanceLexer() // for
        var ok = true
        ok = ok && b.mceIdentifier(VAR_NMI)
        ok = ok && b.consumeToken(IN, true)
        ok = ok && GdExprParser.INSTANCE.parse(b)
        ok = ok && b.consumeToken(COLON, true)
        ok = ok && GdStmtParser.INSTANCE.parse(b)

        return ok
    }

}
