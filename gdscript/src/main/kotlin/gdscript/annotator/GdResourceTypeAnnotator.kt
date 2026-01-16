package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import gdscript.GdScriptBundle
import gdscript.highlighter.GdHighlighterColors
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdNodePath
import gdscript.psi.GdTypes
import gdscript.psi.utils.GdNodeUtil
import gdscript.settings.GdProjectSettingsState
import gdscript.settings.GdProjectState

/**
 * Checks for existence of [res://] resource
 * Checks for existence of $NodePath, %Unique
 * Colors string's formatter specifiers
 */
class GdResourceTypeAnnotator : Annotator {

    val FORMATTER = "((?<!%)%[scdoxXf0-9+.*\\-]+)|(\\{[a-zA-Z0-9]*})".toRegex()

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val state = GdProjectSettingsState.getInstance(element).state.annotators

        if (element is GdNodePath) {
            if (state != GdProjectState.OFF) resourceExists(element, holder, state)
        } else if (element.elementType == GdTypes.STRING) {
            if (state != GdProjectState.OFF)
                if (!stringResourceExists(element, holder, state)) return
            stringFormats(element, holder)
        }
    }

    private fun stringResourceExists(element: PsiElement, holder: AnnotationHolder, state: String): Boolean {
        val text = element.text.trim('"', '\'')
        if (text.startsWith("res://") && GdFileResIndex.getFiles(text, element.project).isEmpty()) {
            holder
                .newAnnotationGd(
                    element.project,
                    GdProjectState.selectedLevel(state),
                    GdScriptBundle.message("annotator.resource.not.found")
                )
                .range(TextRange.create(element.textRange.startOffset + 1, element.textRange.endOffset - 1))
                .create()
            return false
        }
        return true
    }

    private fun resourceExists(element: GdNodePath, holder: AnnotationHolder, state: String) {
        if (element.text == "$\".\"") return
        val node = GdNodeUtil.findNode(element)
        if (node != null) return

        holder
            .newAnnotationGd(element.project, GdProjectState.selectedLevel(state), GdScriptBundle.message("annotator.node.not.found"))
            .range(element.textRange)
            .create()
    }

    private fun stringFormats(element: PsiElement, holder: AnnotationHolder) {
        val offset = element.textOffset
        FORMATTER.findAll(element.text).forEach {
            val range = it.range
            holder
                .newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(TextRange(range.first + offset, range.last + offset + 1))
                .textAttributes(GdHighlighterColors.STRING_FORMAT)
                .create()
        }
    }


}
