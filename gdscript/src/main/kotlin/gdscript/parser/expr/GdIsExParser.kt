//package gdscript.parser.expr
//
//import com.intellij.lang.PsiBuilder
//import com.intellij.psi.tree.IElementType
//import gdscript.parser.common.GdTypedParser
//import gdscript.psi.GdTypes.IS
//import gdscript.psi.GdTypes.IS_EX
//import gdscript.utils.PsiBuilderUtil.consumeToken
//
//object GdIsExParser : GdExprBaseParser {
//
//    override val EXPR_TYPE: IElementType = IS_EX
//
//    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
//        val m = b.mark()
//        var ok = true
//        ok = ok && b.consumeToken(IS, true)
//        ok = ok && GdTypedParser.typedVal(b, false)
//
//        if (ok) m.drop()
//        else m.rollbackTo()
//
//        return ok
//    }
//
//}
