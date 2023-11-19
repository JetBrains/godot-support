package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.AWAIT
import gdscript.psi.GdTypes.AWAIT_EX
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdAwaitExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = AWAIT_EX

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(AWAIT)) return optional

        val m = b.mark()
        var ok = true
        ok = ok && b.consumeToken(AWAIT, true)
        ok = ok && GdExprParser.parse(b)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok || optional
    }

}
