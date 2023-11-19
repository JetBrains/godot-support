package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.BIT_NOT_EX
import gdscript.psi.GdTypes.NOT
import gdscript.utils.PsiBuilderUtil.consumeToken

object GdBitNotExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = BIT_NOT_EX

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        ok = ok && b.consumeToken(NOT, true)
        ok = ok && GdExprParser.parse(b)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
