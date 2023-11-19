package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdPrimaryExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = PRIMARY_EX

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        return b.nextTokenIs(NODE_PATH, NODE_PATH_LEX)
                || arrayDecl(b)
        // TODO dictDecl
        // TODO (LRBR expr RRBR)
    }

    private fun arrayDecl(b: PsiBuilder): Boolean {
        val array = b.mark()
        var ok = true

        ok = ok && b.consumeToken(LSBR)
        while (ok && GdExprParser.parse(b, true)) {
            if (!b.nextTokenIs(COMMA)) break
        }
        ok = ok && b.consumeToken(RSBR)

        if (ok) array.done(ARRAY_DECL)
        else array.rollbackTo()

        return ok
    }

}
