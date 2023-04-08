package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import gdscript.action.quickFix.GdRemoveAnnotationAction
import gdscript.psi.GdAnnotationTl
import gdscript.psi.utils.GdExprUtil
import gdscript.utils.GdAnnotationUtil

/**
 * Check if annotation exists & tool is not within inner class
 */
class GdAnnotationAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdAnnotationTl) return

        val definition = GdAnnotationUtil.get(element)
        if (definition == null) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Unknown annotation")
                .range(element.textRange)
                .withFix(GdRemoveAnnotationAction(element))
                .create()
            return
        }

        val definitionParams = definition.parameters
        val usedParams = element.annotationParams?.literalExList ?: emptyList()

        // Check number of arguments
        if (!definition.variadic) {
            if (usedParams.size > definitionParams.size) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR, "Too many arguments")
                    .range(element.textRange)
                    .create()
                return
            }
        }

        var index = 0
        definitionParams.forEach {
            val name = it.key
            val expectedType = it.value
            if (usedParams.size <= index) return@forEach
            val actualType = usedParams[index++]

            if (!GdExprUtil.typeAccepts(actualType.returnType, expectedType, element.project)) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR, "")
                    .tooltip("<html><body>Type mismatch for $name<table><tr><td>Required:</td><td>$expectedType</td></tr><tr><td>Found:</td><td>$actualType</td></tr></table></html></body>")
                    .range(actualType.textRange)
                    .create()
            }
        }
    }

}