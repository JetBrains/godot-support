package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import gdscript.GdKeywords
import gdscript.completion.GdLookup

object GdClassVarCompletionUtil {

    fun annotations(result: CompletionResultSet, withPrefix: Boolean = true) {
        GdKeywords.ANNOTATIONS_ALL.forEach {
            result
                .addElement(
                    GdLookup.create(
                        // TODO tohle je aktuálně bug IntelliJ, které hard-code odstraňuje @
                        // https://intellij-support.jetbrains.com/hc/en-us/community/posts/8389906293394-Completion-contributor-hard-coded-trims-
//                        if (withPrefix) "@$it" else it,
                        "@$it",
                        // TODO ii params? přidat kdyžtak alespoň závorky
                        //lookup = if (GdKeywords.ANNOTATIONS_PARAMETRIZED.contains(it)) "" else " ",
                        color = GdLookup.ANNOTATOR_COLOR,
                        priority = GdLookup.BUILT_IN,
                    )
                );
        }
    }

}
