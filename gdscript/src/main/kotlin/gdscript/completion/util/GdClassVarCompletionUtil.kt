package gdscript.completion.util

import com.intellij.codeInsight.completion.CompletionResultSet
import gdscript.GdKeywords
import gdscript.completion.GdLookup

object GdClassVarCompletionUtil {

    fun annotations(result: CompletionResultSet) {
        GdKeywords.ANNOTATIONS.forEach {
            result
                .addElement(
                    GdLookup.create(String.format("@%s", it),
                        lookup = " ",
                        color = GdLookup.ANNOTATOR_COLOR)
                );
        }
    }

}
