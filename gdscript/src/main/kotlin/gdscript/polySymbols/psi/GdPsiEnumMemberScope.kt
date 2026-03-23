package gdscript.polySymbols.psi

import com.intellij.model.Pointer
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.PolySymbolQualifiedName
import com.intellij.polySymbols.completion.PolySymbolCodeCompletionItem
import com.intellij.polySymbols.query.PolySymbolCodeCompletionQueryParams
import com.intellij.polySymbols.query.PolySymbolListSymbolsQueryParams
import com.intellij.polySymbols.query.PolySymbolNameMatchQueryParams
import com.intellij.polySymbols.query.PolySymbolQueryStack
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.utils.match
import com.intellij.psi.createSmartPointer
import com.intellij.psi.util.PsiTreeUtil
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.gdCodeCompletions
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdEnumValue

class GdPsiEnumMemberScope(
    private val enumElement: GdEnumDeclTl,
) : PolySymbolScope {

    override fun createPointer(): Pointer<out PolySymbolScope> {
        val ptr = enumElement.createSmartPointer()
        return Pointer { ptr.element?.let { GdPsiEnumMemberScope(it) } }
    }

    private fun enumValueSymbols(): List<PolySymbol> =
        PsiTreeUtil.getChildrenOfTypeAsList(enumElement, GdEnumValue::class.java)
            .map { GdPsiEnumValueSymbol(it.enumValueNmi) }


    override fun getMatchingSymbols(
        qualifiedName: PolySymbolQualifiedName,
        params: PolySymbolNameMatchQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbol> {
        val kind = qualifiedName.kind
        if (kind != GdPolySymbolKind.ENUM_VALUE) return emptyList()

        val name = qualifiedName.name
        return enumValueSymbols()
            .flatMap { it.match(name, params, stack) }
    }

    override fun getSymbols(
        kind: PolySymbolKind,
        params: PolySymbolListSymbolsQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbol> {
        if (kind != GdPolySymbolKind.ENUM_VALUE) return emptyList()
        return enumValueSymbols()
    }

    override fun getCodeCompletions(
        qualifiedName: PolySymbolQualifiedName,
        params: PolySymbolCodeCompletionQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbolCodeCompletionItem> = gdCodeCompletions(qualifiedName, params, stack)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GdPsiEnumMemberScope) return false
        return enumElement == other.enumElement
    }

    override fun hashCode(): Int = enumElement.hashCode()
}