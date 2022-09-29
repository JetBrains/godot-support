package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PsiJavaPatterns.psiElement
import gdscript.GdKeywords
import gdscript.completion.utils.GdLiteralCompletionUtil
import gdscript.completion.utils.GdMethodCompletionUtil
import gdscript.psi.GdTypeHintNm
import gdscript.psi.GdTypes
import gdscript.psi.utils.PsiGdMethodDeclUtil

class GdMethodDeclCompletionContributor : CompletionContributor() {

    val RETURN_TYPE = psiElement().withParent(GdTypeHintNm::class.java)
    val FN_NAME = psiElement().withParent(psiElement(GdTypes.METHOD_ID_NMI));

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (RETURN_TYPE.accepts(parameters.position)) {
            GdLiteralCompletionUtil.builtInTypes(result);
            result.addElement(GdLookup.create(GdKeywords.VOID))
        } else if (FN_NAME.accepts(parameters.position)) {
            val list = PsiGdMethodDeclUtil.collectParentsMethods(parameters.originalFile);
            GdMethodCompletionUtil.addMethods(list, result);
        }
    }

}