package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.ATTRIBUTE_EX
import gdscript.psi.GdTypes.DOT
import gdscript.utils.PsiBuilderUtil.consumeToken

object GdAttributeExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = ATTRIBUTE_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        ok = ok && b.consumeToken(DOT, true)
        ok = ok && GdExprParser.parse(b, false)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
