package gdscript.polySymbols.psi

import com.intellij.model.Pointer
import com.intellij.model.Symbol
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.utils.PolySymbolDeclaredInPsi
import com.intellij.psi.PsiElement
import gdscript.polySymbols.GdNavigationElementProperty
import gdscript.polySymbols.GdPolySymbol
import gdscript.polySymbols.GdPsiSourceElementProperty
import gdscript.psi.GdNamedElement
import gdscript.psi.utils.GdCommonUtil

abstract class GdPsiPolySymbol : GdPolySymbol(), PolySymbolDeclaredInPsi {
    @PolySymbol.Property(GdPsiSourceElementProperty::class)
    abstract override val sourceElement: PsiElement

    @PolySymbol.Property(GdNavigationElementProperty::class)
    private val navigationElement: PsiElement get() = sourceElement

    override val name: String get() = (sourceElement as? GdNamedElement)?.name ?: ""
    protected val project: Project get() = sourceElement.project
    override val textRangeInSourceElement: TextRange? get() = TextRange(0, sourceElement.textLength)
    override val psiContext: PsiElement? get() = sourceElement
    override val returnType: String get() = GdCommonUtil.returnType(sourceElement.parent)

    /**
     * Exposes [sourceElement]'s own dictionary-literal value (if it has one) as a
     * [gdscript.polySymbols.GdPolySymbolKind.DICT_KEY] scope - covers both `dict.key1` (where
     * [sourceElement]'s parent is the dict's own declaration statement) and chaining
     * `dict.key1.key11` (where [sourceElement] is itself a dict key, and its parent is the
     * enclosing [gdscript.psi.GdKeyValue]) with a single override, since
     * [gdscript.polySymbols.scope.GdQualifiedRefIdResolveScope]'s qualifier resolution picks up any
     * referenced symbol whose [queryScope] is non-empty. A no-op (`emptyList()`) for every symbol
     * with no dictionary-literal child, e.g. [GdPsiClassSymbol] already overrides [queryScope]
     * itself, so this default never even runs there.
     */
    override val queryScope: List<PolySymbolScope>
        get() = listOfNotNull(GdPsiPolySymbolUtil.dictKeyQueryScope(sourceElement.parent))

    override fun isEquivalentTo(symbol: Symbol): Boolean =
        this === symbol || (symbol is GdPsiPolySymbol && sourceElement == symbol.sourceElement)

    override fun equals(other: Any?): Boolean =
        this === other || (other is GdPsiPolySymbol && this::class == other::class && sourceElement == other.sourceElement)

    override fun hashCode(): Int = sourceElement.hashCode()

    abstract override fun createPointer(): Pointer<out GdPsiPolySymbol>
}

