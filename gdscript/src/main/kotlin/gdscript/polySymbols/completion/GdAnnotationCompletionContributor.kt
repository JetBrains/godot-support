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
import gdscript.psi.GdAnnotationType

class GdAnnotationCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement().withParent(GdAnnotationType::class.java),
            GdAnnotationCompletionProvider(),
        )
    }
}

private class GdAnnotationCompletionProvider : PolySymbolsCompletionProviderBase<GdAnnotationType>() {

    override fun getContext(position: PsiElement): GdAnnotationType? =
        position.parent?.asSafely<GdAnnotationType>()

    override fun addCompletions(
        parameters: CompletionParameters,
        result: CompletionResultSet,
        position: Int,
        name: String,
        queryExecutor: PolySymbolQueryExecutor,
        context: GdAnnotationType,
    ) {
        processCompletionQueryResults(
            queryExecutor, result, GdPolySymbolKind.ANNOTATION, name, position, context,
        ) { item ->
            item.addToResult(parameters, result)
        }
    }
}
