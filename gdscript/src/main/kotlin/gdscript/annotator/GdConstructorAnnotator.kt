package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.action.quickFix.GdChangeReturnType
import gdscript.action.quickFix.GdRemoveElementAction
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdParentMethodCall

class GdConstructorAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdMethodDeclTl) {
            return;
        }

        if (element.isConstructor) {
            val hint = element.returnType;
            if (hint != "" && hint != "void") {
                val hintEl = element.returnHint;
                if (hintEl != null) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR, "Constructor can return only void")
                    .range(hintEl.textRange)
                    .withFix(GdChangeReturnType(hintEl.returnHintVal, "void"))
                    .create()
                }
            }
        } else {
            val parentCall = PsiTreeUtil.getChildOfType(element, GdParentMethodCall::class.java);
            if (parentCall != null) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR, "Parent call allowed only in constructor")
                    .range(parentCall.textRange)
                    .withFix(GdRemoveElementAction(parentCall))
                    .create()
            }
        }
    }

}
