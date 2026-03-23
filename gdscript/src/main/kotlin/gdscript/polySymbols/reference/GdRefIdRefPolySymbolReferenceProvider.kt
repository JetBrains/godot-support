package gdscript.polySymbols.reference

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.intellij.polySymbols.references.PsiPolySymbolReferenceProvider
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.psi.GdRefIdRef

class GdRefIdRefPolySymbolReferenceProvider : PsiPolySymbolReferenceProvider<GdRefIdRef>{

    override fun getReferencedSymbol(psiElement: GdRefIdRef): PolySymbol? {
        val executor = PolySymbolQueryExecutorFactory.create(psiElement)
        val resolved = executor.nameMatchQuery(GdPolySymbolKind.QUALIFIABLE_SYMBOLS, psiElement.text).run()
        return resolved.firstOrNull()
    }
}
