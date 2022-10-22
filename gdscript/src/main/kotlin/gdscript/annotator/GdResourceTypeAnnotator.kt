package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import gdscript.completion.utils.GdFileCompletionUtil
import gdscript.highlighter.GdHighlighterColors
import gdscript.psi.GdNodePath
import gdscript.psi.GdTypes
import gdscript.psi.utils.PsiGdResourceUtil

class GdResourceTypeAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is GdNodePath) {
            if (element.text.startsWith("$\"%")) {
                holder
                    .newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(TextRange.create(element.textRange.startOffset + 2, element.textRange.endOffset - 1))
                    .textAttributes(GdHighlighterColors.STRING)
                    .create();
            }
            return;
        }

        if (element.elementType == GdTypes.STRING) {
            val text = element.text.trim('"');
            if (text.startsWith("res://") && !PsiGdResourceUtil.resourceExists(text, element.project)) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR, "Resource not found")
                    .range(TextRange.create(element.textRange.startOffset + 1, element.textRange.endOffset - 1))
                    .create();
            }
        }
    }

}
