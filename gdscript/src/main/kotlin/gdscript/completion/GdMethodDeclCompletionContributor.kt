package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns.psiElement
import gdscript.completion.utils.GdMethodCompletionUtil.overrideLookupElement
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.psi.GdTypes
import gdscript.utils.CompletionParametersUtil.indent

/**
 * Override parent methods (after 'func' keyword)
 */
class GdMethodDeclCompletionContributor : CompletionContributor() {

    val METHOD_ID = psiElement().withParent(psiElement(GdTypes.METHOD_ID_NMI))

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val element = parameters.originalPosition ?: return
        if (METHOD_ID.accepts(parameters.position)) {
            val parentClass = GdSymbolResolverUtil.resolveOwnClassSymbol(element)?.resolveSuperClassSymbol() ?: return
            GdSymbolResolverUtil.listMethodSymbols(parentClass)
                .forEach { result.addElement(overrideLookupElement(it, true, parameters.indent())) }
        }
    }

}
