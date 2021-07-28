package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PsiJavaPatterns
import gdscript.completion.util.GdClassCompletionUtil
import gdscript.completion.util.GdLiteralCompletionUtil
import gdscript.psi.GdTyped

class GdTypeHintCompletionContributor : CompletionContributor() {

    val TYPED = PsiJavaPatterns.psiElement().afterLeaf(PsiJavaPatterns.psiElement().withText(":"))
        .withSuperParent(2, GdTyped::class.java);

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (!TYPED.accepts(parameters.position)) {
            return
        }

        GdLiteralCompletionUtil.builtInTypes(result);
    }

}