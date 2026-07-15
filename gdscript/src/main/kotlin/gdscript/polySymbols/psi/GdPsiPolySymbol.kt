package gdscript.polySymbols.psi

import com.intellij.model.Pointer
import com.intellij.model.Symbol
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.polySymbols.utils.PolySymbolDeclaredInPsi
import com.intellij.psi.PsiElement
import gdscript.polySymbols.GdPolySymbol
import gdscript.psi.GdNamedElement
import gdscript.psi.utils.GdCommonUtil

abstract class GdPsiPolySymbol : GdPolySymbol(), PolySymbolDeclaredInPsi {
    abstract override val sourceElement: PsiElement
    override val name: String get() = (sourceElement as? GdNamedElement)?.name ?: ""
    protected val project: Project get() = sourceElement.project
    override val textRangeInSourceElement: TextRange? get() = TextRange(0, sourceElement.textLength)
    override val psiContext: PsiElement? get() = sourceElement
    override val returnType: String get() = GdCommonUtil.returnType(sourceElement.parent)

    override fun isEquivalentTo(symbol: Symbol): Boolean =
        this === symbol || (symbol is GdPsiPolySymbol && sourceElement == symbol.sourceElement)

    override fun equals(other: Any?): Boolean =
        this === other || (other is GdPsiPolySymbol && this::class == other::class && sourceElement == other.sourceElement)

    override fun hashCode(): Int = sourceElement.hashCode()

    abstract override fun createPointer(): Pointer<out GdPsiPolySymbol>
}

