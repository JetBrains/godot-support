package gdscript.polySymbols.psi

import com.intellij.model.Pointer
import com.intellij.model.Symbol
import com.intellij.navigation.SymbolNavigationService
import com.intellij.openapi.project.Project
import com.intellij.platform.backend.navigation.NavigationTarget
import com.intellij.polySymbols.search.PsiLinkedPolySymbol
import com.intellij.psi.PsiElement
import gdscript.polySymbols.GdPolySymbol
import gdscript.psi.GdNamedElement
import gdscript.psi.utils.GdCommonUtil

abstract class GdPsiPolySymbol : GdPolySymbol(), PsiLinkedPolySymbol {
    override val name: String get() = (linkedElement as? GdNamedElement)?.name ?: ""
    protected abstract val project: Project
    abstract override val linkedElement: PsiElement
    override val psiContext: PsiElement? get() = linkedElement
    override val returnType: String get() = GdCommonUtil.returnType(linkedElement.parent)

    override fun getNavigationTargets(project: Project): Collection<NavigationTarget> {
        return listOf(SymbolNavigationService.getInstance().psiElementNavigationTarget(linkedElement))
    }
    override fun isEquivalentTo(symbol: Symbol): Boolean {
        if (this === symbol) return true
        if (symbol is GdPsiPolySymbol && linkedElement == symbol.linkedElement) return true
        return super<PsiLinkedPolySymbol>.isEquivalentTo(symbol)
    }

    abstract override fun createPointer(): Pointer<out GdPsiPolySymbol>
}

