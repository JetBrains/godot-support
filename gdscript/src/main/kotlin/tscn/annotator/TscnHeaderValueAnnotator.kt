package tscn.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.intellij.psi.util.prevLeaf
import tscn.highlighter.TscnHighlighterColors
import tscn.psi.TscnHeaderValueVal
import tscn.psi.TscnTypes

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
        if (element.firstChild?.elementType != TscnTypes.STRING_VALUE) return null;
        if (isNodeType(element)) return TscnHighlighterColors.NODE_TYPE
        if (isMethodType(element)) return TscnHighlighterColors.MEMBER_REF
        return null
    }

    private fun isNodeType(element: PsiElement) : Boolean {
        return element.prevLeaf(true)?.prevLeaf(true)?.textMatches("type") ?: false
    }

    private fun isMethodType(element: PsiElement) : Boolean {
        return element.prevLeaf(true)?.prevLeaf(true)?.textMatches("method") ?: false
    }
}