package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import gdscript.GdKeywords
import gdscript.highlighter.GdHighlighterColors
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.GdTypeHintNm

/**
 * Checks that given return type is valid (built-in or Class)
 */
class GdTypeHintAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdTypeHintNm) return;

        val name = element.text;
        if (name == GdKeywords.VOID) return;

        var color = GdHighlighterColors.CLASS_TYPE;
        if (GdKeywords.BUILT_TYPES.contains(name)) {
            color = GdHighlighterColors.KEYWORD;
        }

        if (GdClassNamingIndex.getGlobally(name, element).isNotEmpty()) {
            holder
                .newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.textRange)
                .textAttributes(color)
                .create()
        } else {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Invalid type")
                .range(element.textRange)
                .create();
        }
    }

}
