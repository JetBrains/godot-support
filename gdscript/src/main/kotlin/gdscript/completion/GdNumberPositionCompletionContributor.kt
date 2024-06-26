package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns
import gdscript.completion.GdCommonContribution.COMMENT_POS
import gdscript.psi.GdTypes

class GdNumberPositionCompletionContributor : CompletionContributor() {

    companion object {
        val NUMBER_POSITION = PlatformPatterns.psiElement(GdTypes.IDENTIFIER)
            .afterLeaf(PlatformPatterns.psiElement())
            .afterLeaf(PlatformPatterns.psiElement(GdTypes.NUMBER))
    }

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (COMMENT_POS.accepts(parameters.position)) {
            result.stopHere()
            return
        }

        if (NUMBER_POSITION.accepts(parameters.position)) {
            result.stopHere()
            return
        }
    }

}
