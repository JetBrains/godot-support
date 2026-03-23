package gdscript.polySymbols.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.polySymbols.completion.PolySymbolsCompletionProviderBase
import com.intellij.polySymbols.query.PolySymbolQueryExecutor
import com.intellij.psi.PsiElement
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.psi.GdPsiMethodSymbol
import gdscript.psi.GdGetMethodIdRef
import gdscript.psi.GdSetMethodIdRef

class GdGetSetMethodIdRefCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement().withParent(
                PlatformPatterns.or(
                    psiElement(GdGetMethodIdRef::class.java),
                    psiElement(GdSetMethodIdRef::class.java),
                ),
            ),
            GdGetSetMethodIdRefCompletionProvider(),
        )
    }
}

private class GdGetSetMethodIdRefCompletionProvider : PolySymbolsCompletionProviderBase<PsiElement>() {

    override fun getContext(position: PsiElement): PsiElement? =
        position.parent?.takeIf { it is GdGetMethodIdRef || it is GdSetMethodIdRef }

    override fun addCompletions(
        parameters: CompletionParameters,
        result: CompletionResultSet,
        position: Int,
        name: String,
        queryExecutor: PolySymbolQueryExecutor,
        context: PsiElement,
    ) {
        processCompletionQueryResults(
            queryExecutor, result, GdPolySymbolKind.METHOD, name, position, context,
        ) { item ->
            item.addToResult(parameters, result)
        }
    }
}
