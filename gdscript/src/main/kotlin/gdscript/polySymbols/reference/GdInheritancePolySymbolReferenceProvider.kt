package gdscript.polySymbols.reference

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.intellij.polySymbols.references.PsiPolySymbolReferenceProvider
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.psi.GdInheritanceIdRef

class GdInheritancePolySymbolReferenceProvider : PsiPolySymbolReferenceProvider<GdInheritanceIdRef> {

    override fun getReferencedSymbol(psiElement: GdInheritanceIdRef): PolySymbol? {
        val executor = PolySymbolQueryExecutorFactory.create(psiElement)
        val resolved = executor.nameMatchQuery(GdPolySymbolKind.INHERITANCE_SYMBOLS, psiElement.text).run()
        return resolved.firstOrNull()
    }
}
