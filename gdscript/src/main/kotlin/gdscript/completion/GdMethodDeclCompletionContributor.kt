package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import gdscript.completion.utils.GdMethodCompletionUtil.lookupDeclaration
import gdscript.psi.GdTypes
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassMemberUtil.methods
import gdscript.psi.utils.GdInheritanceUtil

/**
 * Override parent methods (after 'func' keyword)
 */
class GdMethodDeclCompletionContributor : CompletionContributor() {

    val METHOD_ID = psiElement().withParent(psiElement(GdTypes.METHOD_ID_NMI));

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val element = parameters.originalPosition ?: return;
        if (METHOD_ID.accepts(parameters.position)) {
            val parent = GdInheritanceUtil.getExtendedElement(element) ?: return;
            val list = mutableListOf<PsiElement>()
            GdClassMemberUtil.collectFromParents(parent, list);
            list
                .toTypedArray()
                .methods()
                .forEach { result.addElement(it.lookupDeclaration(true)) };
        }
    }

}