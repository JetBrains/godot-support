package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdNodePath
import gdscript.psi.GdTypes
import gdscript.psi.utils.GdNodeUtil

/**
 * Checks for existence of [res://] resource
 * Checks for existence of $NodePath, %Unique
 */
class GdResourceTypeAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is GdNodePath) {
            resourceExists(element, holder)
        } else if (element.elementType == GdTypes.STRING) {
            stringResourceExists(element, holder)
        }
    }

    private fun stringResourceExists(element: PsiElement, holder: AnnotationHolder) {
        val text = element.text.trim('"')
        if (text.startsWith("res://") && GdFileResIndex.getFiles(text, element.project).isEmpty()) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Resource not found")
                .range(TextRange.create(element.textRange.startOffset + 1, element.textRange.endOffset - 1))
                .create();
        }
    }

    private fun resourceExists(element: GdNodePath, holder: AnnotationHolder) {
        if (element.text == "$\".\"") return
        val node = GdNodeUtil.findNode(element)
        if (node != null) return

        holder
            .newAnnotation(HighlightSeverity.ERROR, "Node not found")
            .range(element.textRange)
            .create()
    }

}
