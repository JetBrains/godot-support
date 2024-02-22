package tscn.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.intellij.psi.util.prevLeaf
import tscn.highlighter.TscnHighlighterColors
import tscn.psi.TscnTypes

class TscnAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val colorAttribute = determineColor(element)

        if (colorAttribute != null) {
            holder
                    .newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(element.textRange)
                    .textAttributes(colorAttribute)
                    .create()
        }
    }


    private fun determineColor(element: PsiElement) : TextAttributesKey? {
        return when(element.elementType) {
            TscnTypes.GD_SCENE, TscnTypes.EXT_RESOURCE, TscnTypes.NODE, TscnTypes.CONNECTION -> TscnHighlighterColors.NODE_RESOURCE
            TscnTypes.IDENTIFIER -> determineIdentifierColor(element)
            TscnTypes.HEADER_VALUE_NM -> TscnHighlighterColors.ATTRIBUTES
            TscnTypes.HEADER_VALUE_VAL, TscnTypes.VALUE -> determineAttributeValueColor(element)
            else -> null
        }
    }

    private fun determineAttributeValueColor(element: PsiElement): TextAttributesKey {
        val text = element.text
        val attributeName = element.prevLeaf(true)?.prevLeaf(true)?.text ?: ""
        if (attributeName == "type") return TscnHighlighterColors.NODE_TYPE
        else if (text.startsWith("\"res:")) return TscnHighlighterColors.RES_STRING
        else if (element.text.startsWith("\"")) return TscnHighlighterColors.STRING

        return TscnHighlighterColors.ATTRIBUTE_VALUES
    }

    private fun determineIdentifierColor(element: PsiElement): TextAttributesKey {
        if (element.prevLeaf(true).elementType == TscnTypes.LSBR) {
            return TscnHighlighterColors.NODE_RESOURCE
        }
        return TscnHighlighterColors.ATTRIBUTES
    }

}