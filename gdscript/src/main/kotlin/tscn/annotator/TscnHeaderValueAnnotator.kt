package tscn.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import tscn.highlighter.TscnHighlighterColors
import tscn.psi.TscnHeaderValueNm
import tscn.psi.TscnHeaderValueVal

class TscnHeaderValueAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is TscnHeaderValueVal) return

        val colorAttribute = determineAttributeValueColor(element)
        if (colorAttribute != null) {
            holder
                .newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.textRange)
                .textAttributes(colorAttribute)
                .create()
        }
    }

    private fun determineAttributeValueColor(element: TscnHeaderValueVal): TextAttributesKey? {
        val name = element.parent.firstChild
        if (name !is TscnHeaderValueNm) return null

        return when (name.name) {
            "type" -> TscnHighlighterColors.NODE_TYPE
            "method" -> TscnHighlighterColors.MEMBER_REF
            else -> null
        }
    }

}
