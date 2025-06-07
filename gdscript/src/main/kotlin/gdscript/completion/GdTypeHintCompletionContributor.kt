package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import gdscript.completion.utils.GdLiteralCompletionUtil
import gdscript.completion.utils.GdRefIdCompletionUtil

/**
 * Built-ins completions
 * Variable type as int, String, float, ..
 * Reference to INF, PI, ..
 */
class GdTypeHintCompletionContributor : CompletionContributor() {

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (GdRefIdCompletionUtil.DIRECT_REF.accepts(parameters.position)) {
            // RefId position offers built-ins like PI or INF
            GdLiteralCompletionUtil.builtIns(result);
        }
    }

}
