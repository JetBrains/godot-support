package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdNodePath
import gdscript.psi.GdTypes
import gdscript.psi.utils.GdNodeUtil
import gdscript.settings.GdSettingsState
import gdscript.settings.GdState

/**
 * Checks for existence of [res://] resource
 * Checks for existence of $NodePath, %Unique
 */
class GdResourceTypeAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val state = GdSettingsState.getInstance().state.annotators
        if (state == GdState.OFF) return
        if (element is GdNodePath) {
            resourceExists(element, holder, state)
        } else if (element.elementType == GdTypes.STRING) {
            stringResourceExists(element, holder, state)
        }
    }

    private fun stringResourceExists(element: PsiElement, holder: AnnotationHolder, state: String) {
        val text = element.text.trim('"')
        if (text.startsWith("res://") && GdFileResIndex.getFiles(text, element.project).isEmpty()) {
            holder
                .newAnnotation(GdState.getLevel(state), "Resource not found")
                .range(TextRange.create(element.textRange.startOffset + 1, element.textRange.endOffset - 1))
                .create();
        }
    }

    private fun resourceExists(element: GdNodePath, holder: AnnotationHolder, state: String) {
        if (element.text == "$\".\"") return
        val node = GdNodeUtil.findNode(element)
        if (node != null) return

        holder
            .newAnnotation(GdState.getLevel(state), "Node not found")
            .range(element.textRange)
            .create()
    }

}
