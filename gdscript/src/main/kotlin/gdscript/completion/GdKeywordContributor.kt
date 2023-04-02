package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns.psiElement
import gdscript.GdKeywords
import gdscript.psi.GdTypes
import gdscript.psi.utils.GdClassMemberUtil

object GdKeywordContributor : CompletionContributor() {

    val TO_HINT_KEYWORDS = arrayOf(
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
        "pass",
        "continue",
        "break",
    )

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (psiElement(GdTypes.COMMENT).accepts(parameters.position)) return
        if (GdClassMemberUtil.calledUpon(parameters.position) != null) return

        result.addAllElements(TO_HINT_KEYWORDS.map { GdLookup.create(it, priority = 0.0) })
    }

}
