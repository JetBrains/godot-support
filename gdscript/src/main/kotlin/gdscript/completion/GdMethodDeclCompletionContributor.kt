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
import gdscript.psi.utils.GdInheritanceUtil.getExtendedElement
import gdscript.utils.CompletionParametersUtil.indent

/**
 * Override parent methods (after 'func' keyword)
 */
class GdMethodDeclCompletionContributor : CompletionContributor() {

    val METHOD_ID = psiElement().withParent(psiElement(GdTypes.METHOD_ID_NMI))

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val element = parameters.originalPosition ?: return
        if (METHOD_ID.accepts(parameters.position)) {
            val parent = getExtendedElement(element, element.project) ?: return
            val list = mutableListOf<Any>()
            GdClassMemberUtil.collectFromParents(parent, list, element.project)
            list
                .methods()
                .forEach { result.addElement(it.lookupDeclaration(true, parameters.indent())) }
        }
    }

}
