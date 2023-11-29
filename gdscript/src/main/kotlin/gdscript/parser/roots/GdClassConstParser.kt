//package gdscript.parser.roots
//
//import gdscript.parser.GdBaseParser
//import gdscript.parser.GdPsiBuilder
//import gdscript.parser.recovery.GdRecovery
//import gdscript.psi.GdTypes.*
//
//object GdClassConstParser : GdBaseParser {
//
//    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
//        if (!b.nextTokenIs(CONST)) return optional
//
//        val m = b.mark()
//        b.advance() // const
//        var ok = b.mceIdentifier(VAR_NMI)
//        ok = ok && GdTypedParser.parseWithAssignTypedAndExpr(b, true)
//
//        ok && b.mceEndStmt()
//
//        GdRecovery.topLevel(b)
//        m.done(CONST_DECL_TL)
//
//        return true
//    }
//
//}
