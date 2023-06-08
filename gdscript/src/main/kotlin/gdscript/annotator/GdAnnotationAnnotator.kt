package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import gdscript.action.quickFix.GdRemoveElementsAction
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
                .create()
            return
        }

        val definitionParams = definition.parameters
        val usedParams = element.annotationParams?.exprList ?: emptyList()

        // Check number of arguments
        if (!definition.variadic && usedParams.size > definitionParams.size) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Too many arguments")
                .range(element.textRange)
                .create()
            return
        }
        if (definition.required > 0 && usedParams.size < definition.required) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Not enough arguments")
                .range(element.textRange)
                .create()
            return
        }

        var expectedType = "Variant"
        var name = ""
        val definedKeys = definitionParams.keys.toTypedArray()
        usedParams.forEachIndexed { index, actualType ->
            if (index < definedKeys.size) {
                expectedType = definitionParams[definedKeys[index]] ?: ""
                name = definedKeys[index]
            }

            val actualReturnType = actualType.returnType
            if (!GdExprUtil.typeAccepts(actualReturnType, expectedType, element)) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR, "")
                    .tooltip("<html><body>Type mismatch for $name<table><tr><td>Required:</td><td>$expectedType</td></tr><tr><td>Found:</td><td>$actualReturnType</td></tr></table></html></body>")
                    .range(actualType.textRange)
                    .create()
            }
        }
    }

}
