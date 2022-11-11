package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import gdscript.GdKeywords
import gdscript.completion.GdLookup

object GdLiteralCompletionUtil {

    fun builtIns(result: CompletionResultSet) {
        GdKeywords.LITERALS.forEachIndexed { i, it ->
            result.addElement(
                GdLookup.create(it, priority = GdLookup.KEYWORDS, typed = GdKeywords.LITERAL_TYPES[i])
            )
        }
    }

}
