package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import gdscript.GdKeywords
import gdscript.action.GdRemoveAnnotationAction
import gdscript.psi.GdAnnotation

class GdAnnotationAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdAnnotation) {
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
            }

            previous = previous.prevSibling;
        }
    }

}