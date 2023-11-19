package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken

object GdArrayExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = ARR_EX

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        ok = ok && b.consumeToken(LSBR, true)
        ok = ok && GdExprParser.parse(b, false)
        ok = ok && b.consumeToken(RSBR, true)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
