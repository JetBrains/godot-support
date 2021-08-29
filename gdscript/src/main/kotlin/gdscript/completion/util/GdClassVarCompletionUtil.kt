package gdscript.completion.util

import com.intellij.codeInsight.completion.CompletionResultSet
import gdscript.GdKeywords
import gdscript.completion.GdLookup

object GdClassVarCompletionUtil {

    fun annotations(result: CompletionResultSet, withPrefix: Boolean = true) {
        GdKeywords.ANNOTATIONS.forEach {
            result
                .addElement(
                    GdLookup.create(if (withPrefix) "@$it" else it,
                        lookup = " ",
                        color = GdLookup.ANNOTATOR_COLOR,
                        priority = GdLookup.USER_DEFINED,
                    )
                );
        }
    }

}
