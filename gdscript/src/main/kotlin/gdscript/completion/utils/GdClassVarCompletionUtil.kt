package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.openapi.project.Project
import config.index.impl.GdConfigAnnotationDataIndex
import gdscript.completion.GdLookup

object GdClassVarCompletionUtil {

    fun annotations(result: CompletionResultSet, project: Project, withPrefix: Boolean = true) {
        GdConfigAnnotationDataIndex.INSTANCE.getAllValues(project).forEach {
            val key = it.name
            val params = it.isVariadic || it.params.isNotEmpty()

            result
                .addElement(
                    GdLookup.create(
                        // TODO this is currently an IntelliJ bug, which hard-codes removal of @
                        // https://intellij-support.jetbrains.com/hc/en-us/community/posts/8389906293394-Completion-contributor-hard-coded-trims-
                        if (withPrefix) "@$key" else key,
                        lookup = if (params) "()" else "",
                        color = GdLookup.COLOR_ANNOTATION,
                        priority = GdLookup.BUILT_IN,
                    )
                )
        }
    }

}
