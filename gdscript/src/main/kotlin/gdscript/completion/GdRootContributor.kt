package gdscript.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.completion.utils.GdClassVarCompletionUtil
import gdscript.completion.utils.GdMethodCompletionUtil.overrideLookupElement
import gdscript.polySymbols.GdPolySymbolModifier
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.polySymbols.scope.hasModifier
import gdscript.psi.GdFile
import gdscript.psi.GdTypes
import gdscript.psi.utils.GdNodeUtil
import gdscript.psi.utils.PsiGdFileUtil
import gdscript.utils.CompletionParametersUtil.indent
import gdscript.utils.StringUtil.snakeToPascalCase

/**
 * Root & InnerClass root level completions
 * keywords, functions (+ overrides), annotations, $NodePaths
 */
class GdRootContributor : CompletionContributor() {

    companion object {
        val ROOT_POSITION = psiElement(GdTypes.IDENTIFIER)
            .withParents(
                PsiErrorElement::class.java,
                GdFile::class.java,
            )

        val INNER_CLASS_POSITION = psiElement(GdTypes.IDENTIFIER)
            .withParents(
                GdFile::class.java,
            )

        val ANNOTATOR_DECL = psiElement(GdTypes.ANNOTATOR)
    }

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val position = parameters.position
        val previous = PsiTreeUtil.prevCodeLeaf(position.originalElement)

        if (previous === null || ROOT_POSITION.accepts(position)) {
            result.addElement(GdLookup.create("${GdKeywords.EXTENDS} ", presentable = GdKeywords.EXTENDS))
            // Main class scope
            addClassName(parameters, result)
            addTopLvlDecl(parameters, result)
        } else if (INNER_CLASS_POSITION.accepts(position)) {
            // Inner class
//            addTopLvlDecl(parameters, result)
        } else if (ANNOTATOR_DECL.accepts(position)) {
            // After "@"
            GdClassVarCompletionUtil.annotations(result, position.project, false)
        }
    }

    private fun addClassName(parameters: CompletionParameters, result: CompletionResultSet) {
        val className = PsiGdFileUtil.filename(parameters.position.containingFile).snakeToPascalCase()
        result.addElement(GdLookup.create(GdKeywords.CLASS_NAME, " $className"))
    }

    private fun addTopLvlDecl(parameters: CompletionParameters, result: CompletionResultSet) {
        GdNodeUtil.listNodes(parameters.position).forEach { result.addAllElements(it.variable_lookups()) }
        GdClassVarCompletionUtil.annotations(result, parameters.position.project)

        val ownClass = GdSymbolResolverUtil.resolveOwnClassSymbol(parameters.position)
        val methods = GdSymbolResolverUtil.listMethodSymbols(ownClass).filterNot { it.hasModifier(GdPolySymbolModifier.STATIC) }
        result.addAllElements(methods.map { overrideLookupElement(it, false, parameters.indent()) })
    }

}
