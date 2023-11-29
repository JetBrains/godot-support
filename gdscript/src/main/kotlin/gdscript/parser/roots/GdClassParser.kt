//package gdscript.parser.roots
//
//import com.intellij.lang.PsiBuilder
//import gdscript.parser.GdBaseParser
//import gdscript.parser.GdRootParser
//import gdscript.parser.recovery.GdRecovery
//import gdscript.psi.GdTypes.*
//import gdscript.utils.PsiBuilderUtil.consumeToken
//import gdscript.utils.PsiBuilderUtil.mceIdentifier
//import gdscript.utils.PsiBuilderUtil.nextTokenIs
//
//object GdClassParser : GdBaseParser {
//
//    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
//        if (!b.nextTokenIs(CLASS)) return optional
//
//        val classDecl = b.mark()
//        b.advance() // class
//
//        var ok = true
//        ok = ok && b.mceIdentifier(CLASS_NAME_NMI)
//        ok = ok && GdInheritanceParser.parse(b, true)
//        ok = ok && b.consumeToken(COLON, true)
//        ok = ok && b.consumeToken(NEW_LINE, true)
//        ok = ok && b.consumeToken(INDENT, true)
//
//        while (
//            ok &&
//            (GdInheritanceParser.parse(b)
//                || GdRootParser.topLevelParsers.any { it.parse(b) })
//        ) {
//        }
//        ok = ok && b.consumeToken(DEDENT, true)
//
//        GdRecovery.topLevel(b)
//        if (ok) classDecl.done(CLASS_DECL_TL)
//        else classDecl.error("Expected class content")
//
//        return ok
//    }
//
//}
