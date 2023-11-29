//package gdscript.parser.expr
//
//import com.intellij.lang.PsiBuilder
//import com.intellij.psi.tree.IElementType
//import gdscript.psi.GdTypes.*
//import gdscript.utils.PsiBuilderUtil.consumeToken
//import gdscript.utils.PsiBuilderUtil.nextTokenIs
//
//object GdPrimaryExParser : GdExprBaseParser {
//
//    override val EXPR_TYPE: IElementType = PRIMARY_EX
//
//    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
//        if (b.nextTokenIs(NODE_PATH, NODE_PATH_LEX)) {
//            b.advance()
//            return true
//        }
//
//        return arrayDecl(b)
//            || bracketExpr(b)
//            || dictDecl(b)
//            || optional
//    }
//
//    private fun arrayDecl(b: GdPsiBuilder): Boolean {
//        val array = b.mark()
//        var ok = true
//
//        ok = ok && b.consumeToken(LSBR)
//        while (ok && GdExprParser.parse(b)) {
//            if (!b.consumeToken(COMMA)) break
//        }
//        ok = ok && b.consumeToken(RSBR)
//
//        if (ok) array.done(ARRAY_DECL)
//        else array.rollbackTo()
//
//        return ok
//    }
//
//    private fun dictDecl(b: GdPsiBuilder): Boolean {
//        val m = b.mark()
//        var ok = true
//
//        ok = ok && b.consumeToken(LCBR, true)
//        while (ok && keyValuePair(b)) {
//            if (!b.consumeToken(COMMA)) break
//        }
//
//        ok = ok && b.consumeToken(RCBR, true)
//
//        if (ok) m.done(DICT_DECL)
//        else m.rollbackTo()
//
//        return ok
//    }
//
//    private fun keyValuePair(b: GdPsiBuilder): Boolean {
//        val m = b.mark()
//        var ok = true
//
//        if (b.nextTokenIs(IDENTIFIER)) {
//            ok = ok && b.consumeToken(IDENTIFIER, true)
//            ok = ok && b.consumeToken(EQ, true)
//            ok = ok && GdExprParser.parse(b)
//        } else {
//            ok = ok && GdExprParser.parse(b)
//            ok = ok && b.consumeToken(COLON, true)
//            ok = ok && GdExprParser.parse(b)
//        }
//
//        if (ok) m.done(KEY_VALUE)
//        else m.rollbackTo()
//
//        return ok
//    }
//
//    private fun bracketExpr(b: GdPsiBuilder): Boolean {
//        val m = b.mark()
//        var ok = true
//
//        ok = ok && b.consumeToken(LRBR, true)
//        ok = ok && GdExprParser.parse(b, true)
//        ok = ok && b.consumeToken(RRBR, true)
//
//        if (ok) m.drop()
//        else m.rollbackTo()
//
//        return ok
//    }
//
//}
