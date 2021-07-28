package gdscript.completion.util

import com.intellij.codeInsight.completion.CompletionResultSet
import gdscript.GdKeywords
import gdscript.completion.GdLookup

object GdLiteralCompletionUtil {

    fun builtIns(result: CompletionResultSet) {
        GdKeywords.LITERALS.forEach {
            result.addElement(
                GdLookup.create(it, priority = GdLookup.STATIC)
            )
        }
    }

    fun builtInTypes(result: CompletionResultSet) {
        GdKeywords.BUILT_TYPES.forEach {
            result.addElement(
                GdLookup.create(it, priority = GdLookup.STATIC)
            )
        }
    }

}
