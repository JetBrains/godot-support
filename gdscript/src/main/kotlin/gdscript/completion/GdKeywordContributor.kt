package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import gdscript.GdKeywords

object GdKeywordContributor : CompletionContributor() {

    val TO_HINT_KEYWORDS = arrayOf(
        GdKeywords.FUNC,
        GdKeywords.STATIC,
        GdKeywords.CONST,
        GdKeywords.VAR,
        GdKeywords.CLASS,
        GdKeywords.SIGNAL,
        "pass",
        "continue",
        "break",
    )

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        result.addAllElements(TO_HINT_KEYWORDS.map { GdLookup.create(it, priority = GdLookup.KEYWORDS) })
    }
}
