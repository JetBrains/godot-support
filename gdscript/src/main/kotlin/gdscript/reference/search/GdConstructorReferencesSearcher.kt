package gdscript.reference.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.PsiReference
import com.intellij.psi.search.UsageSearchContext
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.util.Processor
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.utils.PsiGdMethodIdUtil

/**
 * Find Usages on the `_init` constructor also lists `Class.new()` call sites.
 *
 * The default reference search only scans occurrences of the target's name (`_init`),
 * so `new()` calls are never visited. We add an extra word search for `new`; the
 * single-target result processor then keeps only `new` references that resolve back
 * to this `_init` (GdClassMemberReference already maps `new` -> the `_init` methodIdNmi).
 */
class GdConstructorReferencesSearcher : QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters>(true) {

    override fun processQuery(
        queryParameters: ReferencesSearch.SearchParameters,
        consumer: Processor<in PsiReference>,
    ) {
        val target = queryParameters.elementToSearch
        val isConstructor = when (target) {
            is GdMethodIdNmi -> PsiGdMethodIdUtil.isConstructor(target)
            is GdMethodDeclTl -> target.isConstructor
            else -> false
        }
        if (!isConstructor) return

        queryParameters.optimizer.searchWord(
            "new",
            queryParameters.effectiveSearchScope,
            UsageSearchContext.IN_CODE,
            true,
            target,
        )
    }
}
