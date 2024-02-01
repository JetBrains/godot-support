package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import gdscript.GdKeywords

class GdKeywordContributor : CompletionContributor() {

    val TO_HINT_KEYWORDS = arrayOf(
        "pass",
        "continue",
        "break",
    )

    val TO_HINT_KEYWORDS_WITH_SPACE = arrayOf(
        GdKeywords.FUNC,
        GdKeywords.STATIC,
        GdKeywords.MASTER,
        GdKeywords.PUPPET,
        GdKeywords.REMOTE,
        GdKeywords.REMOTE_SYNC,
        GdKeywords.CONST,
        GdKeywords.VAR,
        GdKeywords.CLASS,
        GdKeywords.SIGNAL,
    )

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        result.addAllElements(TO_HINT_KEYWORDS.map { GdLookup.create(it, priority = -100.0) })
        result.addAllElements(TO_HINT_KEYWORDS_WITH_SPACE.map { GdLookup.create("$it ", priority = -100.0, presentable = it) })
    }

}
