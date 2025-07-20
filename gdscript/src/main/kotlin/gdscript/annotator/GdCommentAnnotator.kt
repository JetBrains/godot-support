package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.util.text.findTextRange
import gdscript.highlighter.GdHighlighterColors
import gdscript.settings.GdProjectSettingsState

class GdCommentAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is PsiComment) return

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
            (its[0] as ArrayList<String>).forEach {
                val range = element.text.findTextRange(it) ?: return@forEach
                holder
                    .newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(range.shiftRight(element.textOffset))
                    .textAttributes(its[1] as TextAttributesKey)
                    .create()
            }
        }
    }
}
