package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import gdscript.GdKeywords
import gdscript.highlighter.GdHighlighterColors
import gdscript.index.impl.GdClassIdIndex
import gdscript.index.impl.GdClassNamingIndex
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdTypeHint
import gdscript.psi.GdTypeHintNm

/**
 * Checks that given return type is valid (built-in or Class)
 */
class GdTypeHintAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
//        if (element !is GdTypeHint) return;
        when (element) {
            is GdTypeHint -> existingType(element, holder);
            is GdTypeHintNm -> colorType(element, holder);
        }
        // TODO ii tady pokračovat


//        val name = element.text;
//        if (name == GdKeywords.VOID) return;
//
//        var color = GdHighlighterColors.CLASS_TYPE;
//        if (GdKeywords.BUILT_TYPES.contains(name)) {
//            color = GdHighlighterColors.KEYWORD;
//        }
//
//        if (GdClassNamingIndex.getGlobally(name, element).isNotEmpty()) {
//            holder
//                .newSilentAnnotation(HighlightSeverity.INFORMATION)
//                .range(element.textRange)
//                .textAttributes(color)
//                .create()
//            return;
//        }
//
//        holder
//            .newAnnotation(HighlightSeverity.ERROR, "Invalid type")
//            .range(element.textRange)
//            .create();
    }

    private fun existingType(element: GdTypeHint, holder: AnnotationHolder) {
        if (GdClassIdIndex.getGloballyResolved(element.text, element.project).isNotEmpty()
            || GdFileResIndex.getFiles(element.text.trim('"'), element.project).isNotEmpty()
        ) {
            return;
        }

        holder
            .newAnnotation(HighlightSeverity.ERROR, "Invalid type")
            .range(element.textRange)
            .create();
    }

    private fun colorType(element: GdTypeHintNm, holder: AnnotationHolder) {
        val name = element.text;

        var color = GdHighlighterColors.CLASS_TYPE;
        if (GdKeywords.BUILT_TYPES.contains(name) || name == GdKeywords.VOID) {
            color = GdHighlighterColors.KEYWORD;
        }

        holder
            .newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(color)
            .create();
    }

}
