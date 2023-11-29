//package gdscript.parser.expr
//
//import com.intellij.lang.PsiBuilder
//import com.intellij.psi.tree.IElementType
//import gdscript.psi.GdTypes.*
//import gdscript.utils.PsiBuilderUtil.consumeToken
//
//object GdTernaryExParser : GdExprBaseParser {
//
//    override val EXPR_TYPE: IElementType = TERNARY_EX
//
//    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
//        val m = b.mark()
//        var ok = true
//        ok = ok && b.consumeToken(IF, true)
//        ok = ok && GdExprParser.parse(b, false)
//        ok = ok && b.consumeToken(ELSE, true)
//        ok = ok && GdExprParser.parse(b, false)
//
//        if (ok) m.drop()
//        else m.rollbackTo()
//
//        return ok
//    }
//
//}
