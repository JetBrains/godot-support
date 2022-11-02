package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns.psiElement
import gdscript.completion.utils.GdLiteralCompletionUtil
import gdscript.completion.utils.GdRefIdCompletionUtil
import gdscript.psi.GdTypes

/**
 * Built-ins completions
 * Variable type as int, String, float, ..
 * Reference to INF, PI, ..
 */
class GdTypeHintCompletionContributor : CompletionContributor() {

    val TYPED = psiElement().withParent(psiElement(GdTypes.TYPE_HINT_NM));

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (TYPED.accepts(parameters.position)) {
            // Type - apart from ClassName add also built-in type like int or String
            GdLiteralCompletionUtil.builtInTypes(result);
        } else if (GdRefIdCompletionUtil.DIRECT_REF.accepts(parameters.position)) {
            // RefId position offers built-ins like PI or INF
            GdLiteralCompletionUtil.builtIns(result);
        }
    }

}
