//package gdscript.parser.roots
//
//import com.intellij.lang.PsiBuilder
//import gdscript.parser.GdBaseParser
//import gdscript.parser.recovery.GdRecovery
//import gdscript.psi.GdTypes.*
//import gdscript.utils.PsiBuilderUtil.consumeToken
//import gdscript.utils.PsiBuilderUtil.mceEndStmt
//import gdscript.utils.PsiBuilderUtil.mceIdentifier
//import gdscript.utils.PsiBuilderUtil.nextTokenIs
//
//object GdEnumParser : GdBaseParser {
//
//    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
//        if (!b.nextTokenIs(ENUM)) return optional
//
//        val enumDecl = b.mark()
//        b.advance() // enum
//
//        var ok = true
//        if (b.nextTokenIs(IDENTIFIER)) {
//            ok = ok && b.mceIdentifier(ENUM_DECL_NMI)
//        }
//
//        if (ok && b.nextTokenIs(LCBR)) b.advance()
//
//        while (ok && b.nextTokenIs(IDENTIFIER)) {
//            ok = enumValue(b)
//            if (!b.consumeToken(COMMA)) break
//        }
//
//        ok = ok && b.consumeToken(RCBR, true)
//        ok = ok && b.mceEndStmt()
//
//        GdRecovery.topLevel(b)
//        enumDecl.done(ENUM_DECL_TL)
//
//        return true
//    }
//
//    private fun enumValue(b: GdPsiBuilder): Boolean {
//        val enumValue = b.mark()
//        b.mceIdentifier(ENUM_VALUE_NMI)
//
//        var ok = true
//        if (b.consumeToken(EQ)) {
//            b.consumeToken(PLUS)
//            b.consumeToken(MINUS)
//            ok = b.consumeToken(NUMBER, true)
//        }
//        enumValue.done(ENUM_VALUE)
//
//        return ok
//    }
//
//}
