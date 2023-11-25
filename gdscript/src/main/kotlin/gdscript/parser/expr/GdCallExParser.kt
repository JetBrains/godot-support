package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.common.GdArgListParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken

object GdCallExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = CALL_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        ok = ok && b.consumeToken(LRBR, true)
        ok = ok && GdArgListParser.parse(b, true)
        ok = ok && b.consumeToken(RRBR, true)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
