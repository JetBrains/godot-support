package gdscript.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.search.RequestResultProcessor
import com.intellij.psi.search.UsageSearchContext
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.util.Processor
import gdscript.polySymbols.psi.GdPsiConstructorSymbol
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReference
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdRefIdRef
import gdscript.psi.utils.PsiGdMethodIdUtil

/**
 * Find Usages on the `_init` constructor also lists `Class.new()` call sites.
 *
 * The default reference search only scans occurrences of the target's name (`_init`),
 * so `new()` calls are never visited. We add an extra word search for `new`. Unlike the
 * default single-target processor (which only looks at classic [PsiReference]s), `new` resolves
 * to `_init` exclusively through PolySymbols own-references
 * ([gdscript.psi.impl.GdRefIdRefImpl.getOwnReferences]) — there is no classic reference to find
 * here anymore. [GdNewKeywordResultProcessor] resolves candidates via PolySymbols directly and,
 * once a match is confirmed, hands back a minimal ad-hoc classic [PsiReference] so the standard
 * Find Usages/Rename UI (which operates on [ReferencesSearch] results) can show the `new()` site.
 */
class GdConstructorReferencesSearcher : QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters>(true) {

    override fun processQuery(
        queryParameters: ReferencesSearch.SearchParameters,
        consumer: Processor<in PsiReference>,
    ) {
        val target = queryParameters.elementToSearch
        val targetIdentifier = when (target) {
            is GdMethodIdNmi -> target.takeIf { PsiGdMethodIdUtil.isConstructor(it) }
            is GdMethodDeclTl -> target.methodIdNmi.takeIf { target.isConstructor }
            else -> null
        } ?: return

        queryParameters.optimizer.searchWord(
            "new",
            queryParameters.effectiveSearchScope,
            UsageSearchContext.IN_CODE,
            true,
            target,
            GdNewKeywordResultProcessor(target, targetIdentifier),
        )
    }

    private class GdNewKeywordResultProcessor(
        target: PsiElement,
        private val targetIdentifier: GdMethodIdNmi,
    ) : RequestResultProcessor(target) {

        override fun processTextOccurrence(
            element: PsiElement,
            offsetInElement: Int,
            consumer: Processor<in PsiReference>,
        ): Boolean {
            if (element !is GdRefIdRef || element.text != "new") return true
            val constructor = element.resolveSymbolReference() as? GdPsiConstructorSymbol ?: return true
            return constructor.sourceElement != targetIdentifier
                || consumer.process(GdNewKeywordReference(element, targetIdentifier))
        }
    }

    private class GdNewKeywordReference(
        element: GdRefIdRef,
        private val target: GdMethodIdNmi,
    ) : PsiReferenceBase<GdRefIdRef>(element, TextRange(0, element.textLength)) {
        override fun resolve(): PsiElement = target
    }
}
