package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiJavaPatterns.psiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiWhiteSpace
import gdscript.GdKeywords
import gdscript.completion.util.GdLiteralCompletionUtil
import gdscript.psi.GdTypeHintNm
import gdscript.psi.GdTypes

class GdMethodDeclCompletionContributor : CompletionContributor() {

    val RETURN_TYPE = psiElement().withParent(GdTypeHintNm::class.java)

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (!RETURN_TYPE.accepts(parameters.position)) {
            return;
        }

        GdLiteralCompletionUtil.builtInTypes(result);
        result.addElement(GdLookup.create(GdKeywords.VOID))
    }

}