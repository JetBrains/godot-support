package gdscript.polySymbols.reference

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.intellij.polySymbols.references.PsiPolySymbolReferenceProvider
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.psi.GdGetMethodIdRef
import gdscript.psi.GdRefIdRef

class GdGetMethodIdPolySymbolReferenceProvider : PsiPolySymbolReferenceProvider<GdGetMethodIdRef>{

    override fun getReferencedSymbol(psiElement: GdGetMethodIdRef): PolySymbol? {
        val executor = PolySymbolQueryExecutorFactory.create(psiElement)
        val resolved = executor.nameMatchQuery(GdPolySymbolKind.METHOD, psiElement.text).run()
        return resolved.firstOrNull()
    }
}
