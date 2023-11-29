//package gdscript.parser.stmt
//
//import com.intellij.lang.PsiBuilder
//import com.intellij.psi.tree.IElementType
//import gdscript.parser.expr.GdExprParser
//import gdscript.parser.recovery.GdRecovery
//import gdscript.psi.GdTypes.*
//import gdscript.utils.PsiBuilderUtil.consumeToken
//import gdscript.utils.PsiBuilderUtil.nextTokenIs
//
//object GdIfStmtParser : GdStmtBaseParser {
//
//    override val STMT_TYPE: IElementType = IF_ST
//    override val endWithEndStmt: Boolean = false
//    override val pinnable: Boolean = false
//
//    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
//        if (!b.nextTokenIs(IF)) return optional
//
//        b.advance() // if
//        var ok = true
//        ok = ok && GdExprParser.parse(b)
//        ok = ok && b.consumeToken(COLON, true)
//        ok = ok && GdStmtParser.parse(b)
//
//        while (ok && elifSt(b)) {}
//        ok && elseSt(b)
//
//        return true
//    }
//
//    private fun elifSt(b: GdPsiBuilder): Boolean {
//        if (!b.nextTokenIs(ELIF)) return false
//
//        val elif = b.mark()
//        var ok = true
//        b.advance() // elif
//        ok = ok && GdExprParser.parse(b)
//        ok = ok && b.consumeToken(COLON, true)
//        ok = ok && GdStmtParser.parse(b)
//
//        GdRecovery.stmt(b)
//        elif.done(ELIF_ST)
//
//        return true
//    }
//
//    private fun elseSt(b: GdPsiBuilder): Boolean {
//        if (!b.nextTokenIs(ELSE)) return false
//
//        val elseSt = b.mark()
//        var ok = true
//        b.advance() // else
//        ok = ok && b.consumeToken(COLON, true)
//        ok = ok && GdStmtParser.parse(b)
//
//        GdRecovery.stmt(b)
//        elseSt.done(ELSE_ST)
//
//        return true
//    }
//
//}
