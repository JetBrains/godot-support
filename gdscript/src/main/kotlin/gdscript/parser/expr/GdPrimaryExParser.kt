package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.rollOrDrop

object GdPrimaryExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = PRIMARY_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (b.passToken(NODE_PATH, NODE_PATH_LEX)) {
            return true
        }

        return arrayDecl(b)
            || bracketExpr(b)
            || dictDecl(b)
            || optional
    }

    private fun arrayDecl(b: GdPsiBuilder): Boolean {
        b.enterSection(ARRAY_DECL)
        var ok = true

        ok = ok && b.consumeToken(LSBR)
        while (ok && GdExprParser.parse(b)) {
            if (!b.passToken(COMMA)) break
        }
        ok = ok && b.consumeToken(RSBR)

        return b.exitSection(ok)
    }

    private fun dictDecl(b: GdPsiBuilder): Boolean {
        b.enterSection(DICT_DECL)
        var ok = true

        ok = ok && b.consumeToken(LCBR)
        while (ok && keyValuePair(b)) {
            if (!b.passToken(COMMA)) break
        }

        ok = ok && b.consumeToken(RCBR)

        return b.exitSection(ok)
    }

    private fun keyValuePair(b: GdPsiBuilder): Boolean {
        b.enterSection(KEY_VALUE)
        var ok = true

        if (b.nextTokenIs(IDENTIFIER)) {
            ok = ok && b.consumeToken(IDENTIFIER)
            ok = ok && b.consumeToken(EQ)
            ok = ok && GdExprParser.parse(b)
        } else {
            ok = ok && GdExprParser.parse(b)
            ok = ok && b.consumeToken(COLON)
            ok = ok && GdExprParser.parse(b)
        }

        return b.exitSection(ok)
    }

    private fun bracketExpr(b: GdPsiBuilder): Boolean {
        val m = b.mark()
        var ok = true

        ok = ok && b.consumeToken(LRBR)
        ok = ok && GdExprParser.parse(b)
        ok = ok && b.consumeToken(RRBR)

        return m.rollOrDrop(ok)
    }

}
