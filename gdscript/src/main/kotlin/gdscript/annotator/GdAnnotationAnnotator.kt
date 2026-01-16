package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.text.HtmlBuilder
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.psi.PsiElement
import gdscript.GdScriptBundle
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
                .newAnnotationGd(element.project, HighlightSeverity.ERROR, GdScriptBundle.message("annotator.annotation.unknown"))
                .range(element.textRange)
                .create()
            return
        }

        val definitionParams = definition.parameters
        val usedParams = element.annotationParams?.exprList ?: emptyList()

        // Check number of arguments
        if (!definition.variadic && usedParams.size > definitionParams.size) {
            holder
                .newAnnotationGd(
                    element.project,
                    HighlightSeverity.ERROR,
                    GdScriptBundle.message("annotator.annotation.too.many.arguments")
                )
                .range(element.textRange)
                .create()
            return
        }
        if (definition.required > 0 && usedParams.size < definition.required) {
            holder
                .newAnnotationGd(
                    element.project, HighlightSeverity.ERROR,
                    GdScriptBundle.message("annotator.annotation.not.enough.arguments")
                )
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
                val tooltip = HtmlBuilder()
                    .append(GdScriptBundle.message("annotator.annotation.type.mismatch.text", name))
                    .append(
                        HtmlChunk.tag("table").children(
                            HtmlChunk.tag("tr").children(
                                HtmlChunk.tag("td").addText(GdScriptBundle.message("annotator.required")),
                                HtmlChunk.tag("td").addText(expectedType)
                            ),
                            HtmlChunk.tag("tr").children(
                                HtmlChunk.tag("td").addText(GdScriptBundle.message("annotator.found")),
                                HtmlChunk.tag("td").addText(actualReturnType)
                            )
                        )
                    )
                    .wrapWithHtmlBody()
                    .toString()

                holder
                    .newAnnotationGd(element.project, HighlightSeverity.ERROR, "")
                    .tooltip(tooltip)
                    .range(actualType.textRange)
                    .create()
            }
        }
    }

}
