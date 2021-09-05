package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PsiJavaPatterns.psiElement
import gdscript.completion.util.GdResourceCompletionUtil
import gdscript.psi.GdRefIdNm
import gdscript.psi.GdTypes

class GdResourceCompletionContributor : CompletionContributor() {

    val REF_ID = psiElement().withParent(GdRefIdNm::class.java)
    val NODE_PATH = psiElement(GdTypes.NODE_PATH_LEX);

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (REF_ID.accepts(parameters.position)) {
            GdResourceCompletionUtil.resources(parameters.position.originalElement, result);
        } else if (NODE_PATH.accepts(parameters.position)) {
            GdResourceCompletionUtil.resources(parameters.position.originalElement, result);
        }
    }

}