package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiElement
import gdscript.GdKeywords
import gdscript.psi.GdTypes

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

    val SKIP_KEYWORDS_FOR = PlatformPatterns.or(
        psiElement().afterLeaf(psiElement(GdTypes.DOT)),
        psiElement().withParent(psiElement(GdTypes.INHERITANCE_ID_NM)),
        psiElement().withParent(psiElement(GdTypes.CLASS_NAME_NMI)),
    )

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (SKIP_KEYWORDS_FOR.accepts(parameters.position)) return

        result.addAllElements(TO_HINT_KEYWORDS.map { GdLookup.create(it, priority = -100.0) })
        result.addAllElements(TO_HINT_KEYWORDS_WITH_SPACE.map { GdLookup.create("$it ", priority = -100.0, presentable = it) })
    }

}
