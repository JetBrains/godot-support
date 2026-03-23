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
import gdscript.polySymbols.psi.GdPsiPolySymbolUtil
import gdscript.polySymbols.scope.filterOutNonStaticCompletionItems
import gdscript.psi.GdRefIdRef

class GdRefIdRefCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement().withParent(GdRefIdRef::class.java),
            GdRefIdRefCompletionProvider(),
        )
    }
}

private class GdRefIdRefCompletionProvider : PolySymbolsCompletionProviderBase<GdRefIdRef>() {

    override fun getContext(position: PsiElement): GdRefIdRef? =
        position.parent?.asSafely<GdRefIdRef>()

    override fun addCompletions(
        parameters: CompletionParameters,
        result: CompletionResultSet,
        position: Int,
        name: String,
        queryExecutor: PolySymbolQueryExecutor,
        context: GdRefIdRef,
    ) {
        val requireStatic = GdPsiPolySymbolUtil.isStatic(context)
        val kind = GdPolySymbolKind.QUALIFIABLE_SYMBOLS
        val items = queryExecutor
            .codeCompletionQuery(kind, name, position).run()
        val filtered = if (requireStatic) items.filterOutNonStaticCompletionItems() else items

        processPolySymbolCodeCompletionItems(
            filtered, result, kind, name, queryExecutor.context, context,
        ) { item ->
            if (item.shouldShow())
                item.addToResult(parameters, result)
        }
    }
}
