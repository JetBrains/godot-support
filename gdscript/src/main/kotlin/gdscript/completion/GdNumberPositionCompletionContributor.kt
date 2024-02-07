package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns
import gdscript.psi.GdTypes

class GdNumberPositionCompletionContributor : CompletionContributor() {

    companion object {
        val NUMBER_POSITION = PlatformPatterns.psiElement(GdTypes.IDENTIFIER)
            .afterLeaf(PlatformPatterns.psiElement())
            .afterLeaf(PlatformPatterns.psiElement(GdTypes.NUMBER))
    }

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (NUMBER_POSITION.accepts(parameters.position)) {
            result.stopHere();
        }
    }

}
