package gdscript.search

import com.intellij.model.psi.PsiSymbolReferenceService
import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.util.TextRange
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.utils.PolySymbolDelegate.Companion.unwrapAllDelegates
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.search.RequestResultProcessor
import com.intellij.psi.search.UsageSearchContext
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.util.Processor
import gdscript.polySymbols.gdPsiSourceElement

/**
 * Bridges GDScript's own-references (own [com.intellij.model.psi.PsiSymbolReference]s, e.g.
 * [gdscript.psi.impl.GdRefIdRefImpl.getOwnReferences] and its siblings) into classic
 * [ReferencesSearch] results.
 *
 * GDScript's usage sites (`GdRefIdRef`/`GdTypeHintRef`/`GdInheritanceIdRef`/etc.) resolve
 * exclusively through PolySymbols own-references now; they no longer implement classic
 * [PsiReference]. The platform's default [ReferencesSearch] executor only inspects classic
 * references ([com.intellij.psi.PsiReferenceService]), so it silently finds nothing for these
 * elements. This executor performs the same word-search as the default, but validates each
 * candidate by resolving its own-references and checking whether any of them declares the
 * search target, wrapping a match in a minimal ad-hoc [PsiReference] — the same pattern already
 * proven by [GdConstructorReferencesSearcher] for the "new" -> "_init" case, generalized to any
 * GDScript declaration.
 */
class GdOwnReferencesSearcher : QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters>(true) {

    override fun processQuery(
        queryParameters: ReferencesSearch.SearchParameters,
        consumer: Processor<in PsiReference>,
    ) {
        val target = queryParameters.elementToSearch
        val name = (target as? PsiNamedElement)?.name ?: return

        queryParameters.optimizer.searchWord(
            name,
            queryParameters.effectiveSearchScope,
            UsageSearchContext.IN_CODE,
            true,
            target,
            GdOwnReferenceResultProcessor(target),
        )
    }

    private class GdOwnReferenceResultProcessor(
        private val target: PsiElement,
    ) : RequestResultProcessor(target) {

        override fun processTextOccurrence(
            element: PsiElement,
            offsetInElement: Int,
            consumer: Processor<in PsiReference>,
        ): Boolean {
            val range = ownReferenceRange(element) ?: return true
            return consumer.process(GdOwnPsiReference(element, range, target))
        }

        /**
         * The range of the own reference that resolves to the target, or null when [element] has
         * none. The range excludes what surrounds the name, for example the quotes of a scene
         * value, so a rename through this reference keeps them - see
         * [tscn.psi.manipulator.TscnElementManipulator].
         */
        private fun ownReferenceRange(element: PsiElement): TextRange? =
            PsiSymbolReferenceService.getService().getReferences(element)
                .firstOrNull { reference ->
                    reference.resolveReference()
                        .filterIsInstance<PolySymbol>()
                        .any { it.unwrapAllDelegates().gdPsiSourceElement == target }
                }
                ?.rangeInElement
    }

    private class GdOwnPsiReference(
        element: PsiElement,
        range: TextRange,
        private val target: PsiElement,
    ) : PsiReferenceBase<PsiElement>(element, range) {
        override fun resolve(): PsiElement = target
    }
}
