package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import gdscript.GdKeywords
import gdscript.action.quickFix.GdRemoveAnnotationAction
import gdscript.psi.GdAnnotationTl
import gdscript.psi.GdFile

/**
 * Check if annotation exists & tool is not within inner class
 */
class GdAnnotationAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdAnnotationTl) return;

        val annotation = element.firstChild?.text.orEmpty().trimStart('@');

        exists(annotation, element, holder);
        hasCorrectParameters(annotation, element, holder);
        toolValidation(annotation, element, holder);
    }

    private fun exists(annotation: String, element: GdAnnotationTl, holder: AnnotationHolder) {
        if (!GdKeywords.ANNOTATIONS_ALL.contains(annotation)) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Unknown annotation")
                .range(element.textRange)
                .withFix(GdRemoveAnnotationAction(element))
                .create();
        }
    }

    private fun hasCorrectParameters(annotation: String, element: GdAnnotationTl, holder: AnnotationHolder) {
        if (GdKeywords.ANNOTATIONS_STAND_ALONE.containsKey(annotation) && element.firstChild.nextSibling != null) {
            holder
                .newAnnotation(
                    HighlightSeverity.ERROR,
                    "Standalone annotation [$annotation] cannot be parametrized",
                )
                .range(element.textRange)
                .withFix(GdRemoveAnnotationAction(element))
                .create();
            return;
        }

        // TODO annotation params - tohle je potřeba prozkoumat a sepsat (něco je navíc parametrized)
    }

    private fun toolValidation(annotation: String, element: GdAnnotationTl, holder: AnnotationHolder) {
        if (GdKeywords.ANNOTATIONS_ROOT_ONLY.contains(annotation) && element.parent !is GdFile) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "[$annotation] is not supported in this scope")
                .range(element.textRange)
                .withFix(GdRemoveAnnotationAction(element))
                .create();
        }
    }

}