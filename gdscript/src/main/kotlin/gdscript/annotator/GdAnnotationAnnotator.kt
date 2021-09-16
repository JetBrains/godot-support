package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import gdscript.GdKeywords
import gdscript.action.quickFix.GdRemoveAnnotationAction

class GdAnnotationAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        // TODO předělat
        /*if (element !is GdAnnotation) {
            return;
        }

        val annotation = element.firstChild?.text.orEmpty();
        if (annotation != "" && !GdKeywords.ANNOTATIONS.contains(annotation.substring(1))) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Unsupported annotation")
                .range(element.textRange)
                .withFix(GdRemoveAnnotationAction(element.firstChild))
                .create();
            return;
        }

        var previous = element.prevSibling;
        while (previous !== null) {
            if (previous is GdAnnotation) {
                if (annotation == previous.firstChild?.text) {
                    holder
                        .newAnnotation(HighlightSeverity.ERROR, "Annotation already defined")
                        .range(element.textRange)
                        .withFix(GdRemoveAnnotationAction(element.firstChild))
                        .create();
                    return;
                }
            } else if (previous !is PsiWhiteSpace) {
                break;
            }

            previous = previous.prevSibling;
        }*/
    }

}