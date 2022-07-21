package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import gdscript.GdKeywords
import gdscript.highlighter.GdHighlighterColors
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.GdTypeHintNm

class GdTypeHintAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdTypeHintNm) {
            return;
        }

        val name = element.text;
        if (GdKeywords.BUILT_TYPES.contains(name) || name == GdKeywords.VOID) {
            return;
        }

        if (GdClassNamingIndex.get(
                name,
                element.project,
                GlobalSearchScope.allScope(element.project)
            ).isNotEmpty()
        ) {
            holder
                .newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.textRange)
                .textAttributes(GdHighlighterColors.CLASS_TYPE)
                .create()
            return;
        }

        holder
            .newAnnotation(HighlightSeverity.ERROR, "Invalid type")
            .range(element.textRange)
            .create();
    }

}
