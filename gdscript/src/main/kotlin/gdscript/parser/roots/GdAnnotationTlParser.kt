//package gdscript.parser.roots
//
//import com.intellij.lang.PsiBuilder
//import gdscript.parser.common.GdAnnotationParser
//import gdscript.parser.recovery.GdRecovery
//import gdscript.psi.GdTypes.ANNOTATION_TL
//import gdscript.psi.GdTypes.ANNOTATOR
//import gdscript.utils.PsiBuilderUtil.nextTokenIs
//
//object GdAnnotationTlParser : GdAnnotationParser {
//
//    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
//        if (!b.nextTokenIs(ANNOTATOR)) return optional
//
//        val m = b.mark()
//        val ok = super.parse(b, optional)
//        if (ok) GdRecovery.stmt(b)
//        m.done(ANNOTATION_TL)
//
//        return true
//    }
//
//}
