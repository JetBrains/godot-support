package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns.psiElement
import gdscript.completion.utils.GdLiteralCompletionUtil
import gdscript.completion.utils.GdRefIdCompletionUtil
import gdscript.psi.GdTyped

class GdTypeHintCompletionContributor : CompletionContributor() {

    val TYPED = psiElement().afterLeaf(psiElement().withText(":"))
        .withSuperParent(2, GdTyped::class.java);

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (TYPED.accepts(parameters.position)) {
            GdLiteralCompletionUtil.builtInTypes(result);
        } else if (GdRefIdCompletionUtil.DIRECT_REF.accepts(parameters.position)) {
            GdLiteralCompletionUtil.builtIns(result);
        }

    }

}