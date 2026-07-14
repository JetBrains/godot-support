package gdscript.polySymbols.reference

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.intellij.polySymbols.references.PsiPolySymbolReferenceProvider
import com.intellij.psi.util.PsiTreeUtil
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.scope.filterOutNonStatic
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdGetMethodIdRef

class GdGetMethodIdPolySymbolReferenceProvider : PsiPolySymbolReferenceProvider<GdGetMethodIdRef>{

    override fun getReferencedSymbol(psiElement: GdGetMethodIdRef): PolySymbol? {
        val executor = PolySymbolQueryExecutorFactory.create(psiElement)
        val resolved = executor.nameMatchQuery(GdPolySymbolKind.METHOD, psiElement.text).run()
        val owningVar = PsiTreeUtil.getStubOrPsiParentOfType(psiElement, GdClassVarDeclTl::class.java)
        val filtered = if (owningVar?.isStatic == true) resolved.filterOutNonStatic() else resolved
        return filtered.firstOrNull()
    }
}
