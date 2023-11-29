//package gdscript.parser.expr
//
//import com.intellij.lang.PsiBuilder
//import com.intellij.psi.tree.IElementType
//import gdscript.parser.common.GdTypedParser
//import gdscript.psi.GdTypes.AS
//import gdscript.psi.GdTypes.CAST_EX
//import gdscript.utils.PsiBuilderUtil.consumeToken
//
//object GdCastExParser : GdExprBaseParser {
//
//    override val EXPR_TYPE: IElementType = CAST_EX
//
//    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
//        val m = b.mark()
//        var ok = true
//        ok = ok && b.consumeToken(AS, true)
//        ok = ok && GdTypedParser.typedVal(b, false)
//
//        if (ok) m.drop()
//        else m.rollbackTo()
//
//        return ok
//    }
//
//}
