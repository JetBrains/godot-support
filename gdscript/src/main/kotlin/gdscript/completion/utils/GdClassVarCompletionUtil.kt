package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import gdscript.completion.GdLookup
import gdscript.utils.GdAnnotationUtil

object GdClassVarCompletionUtil {

    fun annotations(result: CompletionResultSet, withPrefix: Boolean = true) {
        GdAnnotationUtil.ANNOTATIONS.forEach {
            val key = it.key
            val params = it.value.variadic || it.value.parameters.isNotEmpty()

            result
                .addElement(
                    GdLookup.create(
                        // TODO tohle je aktuálně bug IntelliJ, které hard-code odstraňuje @
                        // https://intellij-support.jetbrains.com/hc/en-us/community/posts/8389906293394-Completion-contributor-hard-coded-trims-
                        if (withPrefix) "@$key" else key,
                        lookup = if (params) "()" else "",
                        color = GdLookup.COLOR_ANNOTATION,
                        priority = GdLookup.BUILT_IN,
                    )
                );
        }
    }

}
