package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdScriptBundle
import gdscript.psi.*

class GdFlowStmtAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdFlowSt) return
        val txt = element.type
        if (txt != "continue" && txt != "break") return

        PsiTreeUtil.findFirstParent(element) {
            it is GdMethodDeclTl || it is GdFuncDeclEx || it is GdForSt || it is GdWhileSt
        }?.let {
            if (it is GdMethodDeclTl || it is GdFuncDeclEx) {
                holder
                    .newAnnotationGd(
                        element.project,
                        HighlightSeverity.ERROR,
                        GdScriptBundle.message("annotator.cannot.use.text.outside.of.a.loop", txt)
                    )
                    .range(element.textRange)
                    .create()
            }
        }
    }
}
