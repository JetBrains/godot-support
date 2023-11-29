//package gdscript.parser.stmt
//
//import com.intellij.lang.PsiBuilder
//import com.intellij.psi.tree.IElementType
//import gdscript.parser.expr.GdExprParser
//import gdscript.psi.GdTypes.*
//import gdscript.utils.PsiBuilderUtil.consumeToken
//import gdscript.utils.PsiBuilderUtil.mceIdentifier
//import gdscript.utils.PsiBuilderUtil.nextTokenIs
//
//object GdForStmtParser : GdStmtBaseParser {
//
//    override val STMT_TYPE: IElementType = FOR_ST
//    override val endWithEndStmt: Boolean = false
//    override val pinnable: Boolean = false
//
//    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
//        if (!b.nextTokenIs(FOR)) return false
//
//        b.advance() // for
//        var ok = true
//        ok = ok && b.mceIdentifier(VAR_NMI)
//        ok = ok && b.consumeToken(IN, true)
//        ok = ok && GdExprParser.parse(b)
//        ok = ok && b.consumeToken(COLON, true)
//        ok = ok && GdStmtParser.parse(b)
//
//        return true
//    }
//
//}
