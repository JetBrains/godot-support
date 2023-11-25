package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.IN
import gdscript.psi.GdTypes.IN_EX
import gdscript.utils.PsiBuilderUtil.consumeToken

object GdInExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = IN_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        ok = ok && b.consumeToken(IN, true)
        ok = ok && GdExprParser.parse(b, false)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
