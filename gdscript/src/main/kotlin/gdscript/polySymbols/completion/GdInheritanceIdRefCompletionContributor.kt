package gdscript.polySymbols.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns.or
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.polySymbols.completion.PolySymbolsCompletionProviderBase
import com.intellij.polySymbols.query.PolySymbolQueryExecutor
import com.intellij.psi.PsiElement
import com.intellij.util.asSafely
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.psi.GdInheritanceIdRef
import gdscript.psi.GdInheritanceSubIdRef
import gdscript.psi.GdRefElement

class GdInheritanceIdRefCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement().withParent(
                or(
                    psiElement(GdInheritanceIdRef::class.java),
                    psiElement(GdInheritanceSubIdRef::class.java),
                )
            ),
            GdInheritanceIdRefCompletionProvider(),
        )
    }
}

private class GdInheritanceIdRefCompletionProvider : PolySymbolsCompletionProviderBase<GdRefElement>() {

    override fun getContext(position: PsiElement): GdRefElement? =
        position.parent?.asSafely<GdInheritanceIdRef>() ?: position.parent?.asSafely<GdInheritanceSubIdRef>()

    override fun addCompletions(
        parameters: CompletionParameters,
        result: CompletionResultSet,
        position: Int,
        name: String,
        queryExecutor: PolySymbolQueryExecutor,
        context: GdRefElement,
    ) {
        processCompletionQueryResults(
            queryExecutor, result, GdPolySymbolKind.INHERITANCE_SYMBOLS, name, position, context,
        ) { item ->
            if (item.shouldShow())
                item.addToResult(parameters, result)
        }
    }
}
