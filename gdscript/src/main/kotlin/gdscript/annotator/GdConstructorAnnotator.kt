package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.action.quickFix.GdChangeReturnType
import gdscript.action.quickFix.GdRemoveElementAction
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdParentMethodCall
import gdscript.psi.utils.PsiGdClassUtil

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
                    val hintVal = hintEl.returnHintVal;
                    val myClass = PsiGdClassUtil.getClassName(element);
                    if (hint != myClass) {
                        holder
                            .newAnnotation(HighlightSeverity.ERROR, "Constructor can return only Self")
                            .range(hintVal.textRange)
                            .withFix(GdChangeReturnType(hintVal, "void"))
                            .create()
                    }
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
