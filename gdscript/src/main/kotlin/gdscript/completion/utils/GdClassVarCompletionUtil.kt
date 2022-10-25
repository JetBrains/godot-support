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
                        if (withPrefix) "@$it" else it,
                        //lookup = if (GdKeywords.ANNOTATIONS_PARAMETRIZED.contains(it)) "" else " ",
                        color = GdLookup.ANNOTATOR_COLOR,
                        priority = GdLookup.USER_DEFINED,
                    )
                );
        }
    }

}
