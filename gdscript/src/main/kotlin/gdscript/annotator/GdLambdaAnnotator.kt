package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import gdscript.highlighter.GdHighlighterColors
import gdscript.psi.GdFuncDeclIdNmi

/**
 * Colors lambda functions
 */
class GdLambdaAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdFuncDeclIdNmi) return

        holder
            .newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(GdHighlighterColors.METHOD_DECLARATION)
            .create()
    }

}
