package gdscript.highlighter

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.psi.PsiElement
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdParam

internal class GdColorAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GdParam -> {
                val paramName = element.varNmi
                // remove keyword highlighting from func decl params
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(paramName)
                    .textAttributes(HighlighterColors.TEXT)
                    .create()
            }

            is GdClassVarDeclTl -> {
                val paramName = element.varNmi ?: return
                // remove keyword highlighting from top level params
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(paramName)
                    .textAttributes(HighlighterColors.TEXT)
                    .create()
            }
        }
    }
}
