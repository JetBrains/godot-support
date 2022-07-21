package gdscript.completion.util

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

    fun builtInTypes(result: CompletionResultSet) {
        GdKeywords.BUILT_TYPES.forEach {
            result.addElement(
                GdLookup.create(it, priority = GdLookup.KEYWORDS)
            )
        }
    }

}
