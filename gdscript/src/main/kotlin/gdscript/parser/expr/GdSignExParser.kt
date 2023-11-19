package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeAnyOfToken

object GdSignExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = SIGN_EX

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        ok = ok && b.consumeAnyOfToken(true, PLUS, MINUS)
        ok = ok && GdExprParser.parse(b)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
