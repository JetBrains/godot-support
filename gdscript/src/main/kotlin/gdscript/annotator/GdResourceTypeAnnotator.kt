package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import gdscript.highlighter.GdHighlighterColors
import gdscript.psi.GdNodePath

class GdResourceTypeAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        // TODO check exists
        if (element is GdNodePath) {
            if (element.text.startsWith("$\"%")) {
                holder
                    .newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(TextRange.create(element.textRange.startOffset + 2, element.textRange.endOffset - 1))
                    .textAttributes(GdHighlighterColors.STRING)
                    .create();
            }
        }
    }

}
