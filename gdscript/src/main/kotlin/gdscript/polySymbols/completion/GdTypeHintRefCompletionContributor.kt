package gdscript.polySymbols.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.polySymbols.completion.PolySymbolsCompletionProviderBase
import com.intellij.polySymbols.query.PolySymbolQueryExecutor
import com.intellij.psi.PsiElement
import com.intellij.util.asSafely
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.psi.GdTypeHintRef

class GdTypeHintRefCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement().withParent(GdTypeHintRef::class.java),
            GdTypeHintRefCompletionProvider(),
        )
    }
}

private class GdTypeHintRefCompletionProvider : PolySymbolsCompletionProviderBase<GdTypeHintRef>() {

    override fun getContext(position: PsiElement): GdTypeHintRef? =
        position.parent?.asSafely<GdTypeHintRef>()

    override fun addCompletions(
        parameters: CompletionParameters,
        result: CompletionResultSet,
        position: Int,
        name: String,
        queryExecutor: PolySymbolQueryExecutor,
        context: GdTypeHintRef,
    ) {
        processCompletionQueryResults(queryExecutor, result, GdPolySymbolKind.TYPE_HINTS, name, position, context) { item ->
            if (item.shouldShow())
                item.addToResult(parameters, result)
        }
    }
}
