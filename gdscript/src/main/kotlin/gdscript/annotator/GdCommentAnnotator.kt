package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.util.text.findTextRange
import gdscript.highlighter.GdHighlighterColors
import gdscript.settings.GdProjectSettingsState
import gdscript.utils.GdCustomRegionUtil

class GdCommentAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is PsiComment) return

        val markerLength = GdCustomRegionUtil.getMarkerLength(element.text)
        if (markerLength > 0) {
            val markerRange = TextRange(element.textOffset, element.textOffset + markerLength)
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(markerRange)
                .textAttributes(GdHighlighterColors.KEYWORD)
                .create()
            return
        }

        if (element.text.startsWith("##")) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .textAttributes(GdHighlighterColors.DOC_COMMENT)
                .create()
        }

        val state = GdProjectSettingsState.getInstance(element).state
        val criticals = state.criticals.split(",")
        val warnings = state.warnings.split(",")
        val notes = state.notes.split(",")

        arrayOf(
            arrayOf(criticals, GdHighlighterColors.DANGER),
            arrayOf(warnings, GdHighlighterColors.WARNING),
            arrayOf(notes, GdHighlighterColors.NOTE),
        ).forEach { its ->
            (its[0] as ArrayList<*>).forEach {
                val range = element.text.findTextRange(it as String) ?: return@forEach
                holder
                    .newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(range.shiftRight(element.textOffset))
                    .textAttributes(its[1] as TextAttributesKey)
                    .create()
            }
        }
    }
}
