package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.rollOrDrop

object GdPrimaryExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = PRIMARY_EX

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "PrimaryExpr")) return false
        if (b.mcToken(NODE_PATH, NODE_PATH_LEX)) {
            return true
        }

        return arrayDecl(b, l)
            || bracketExpr(b, l)
            || dictDecl(b, l)
            || optional
    }

    private fun arrayDecl(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "ArrayDeclExpr")) return false
        b.enterSection(ARRAY_DECL)
        var ok = b.consumeToken(LSBR, pin = true)
        while (ok && GdExprParser.parse(b, l + 1)) {
            if (!b.passToken(COMMA)) break
        }
        ok = ok && b.consumeToken(RSBR)

        return b.exitSection(ok, true)
    }

    private fun dictDecl(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "DictExpr")) return false
        b.enterSection(DICT_DECL)

        var ok = b.consumeToken(LCBR, pin = true)
        while (ok && keyValuePair(b, l + 1)) {
            if (!b.passToken(COMMA)) break
        }

        ok = ok && b.consumeToken(RCBR)

        return b.exitSection(ok, true)
    }

    private fun keyValuePair(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "KeyValuePairExpr")) return false
        b.enterSection(KEY_VALUE)
        var ok = true

        if (b.followingTokensAre(IDENTIFIER, EQ)) {
            ok = ok && b.consumeToken(IDENTIFIER)
            ok = ok && b.consumeToken(EQ)
            ok = ok && GdExprParser.parse(b, l + 1)
        } else if (b.followingTokensAre(STRING, EQ)) {
            ok = ok && b.consumeToken(STRING)
            ok = ok && b.consumeToken(EQ)
            ok = ok && GdExprParser.parse(b, l + 1)
        } else {
            ok = ok && GdExprParser.parse(b, l + 1)
            ok = ok && b.consumeToken(COLON)
            ok = ok && GdExprParser.parse(b, l + 1)
        }

        return b.exitSection(ok)
    }

    private fun bracketExpr(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "BracketExpr")) return false
        val m = b.mark()
        var ok = true

        ok = ok && b.consumeToken(LRBR)
        ok = ok && GdExprParser.parse(b, l + 1)
        ok = ok && b.consumeToken(RRBR)

        return m.rollOrDrop(ok)
    }

}
