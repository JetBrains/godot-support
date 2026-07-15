package gdscript.polySymbols.scope

import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.PolySymbolQualifiedName
import com.intellij.polySymbols.query.PolySymbolListSymbolsQueryParams
import com.intellij.polySymbols.query.PolySymbolNameMatchQueryParams
import com.intellij.polySymbols.query.PolySymbolQueryStack
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.utils.match
import com.intellij.psi.PsiElement
import com.intellij.psi.createSmartPointer
import gdscript.index.impl.GdClassNamingIndex
import gdscript.polySymbols.GdClassSymbol
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.psi.GdPsiClassSymbolFactory

/**
 * Project-level scope providing PSI-backed class symbols for user-defined GDScript classes.
 */
class GdPsiClassesPolySymbolScope(
    private val project: Project,
    private val location: PsiElement? = null,
) : PolySymbolScope {

    override fun createPointer(): Pointer<out PolySymbolScope> {
        val locationPointer = location?.createSmartPointer()
        return Pointer { GdPsiClassesPolySymbolScope(project, locationPointer?.element) }
    }


    private fun collectMatchingSymbols(name: String): List<GdClassSymbol> {
        return GdClassNamingIndex.INSTANCE.getGlobally(name, project)
            .mapNotNull { GdPsiClassSymbolFactory.create(it) }
    }

    private fun collectAllSymbols(): List<PolySymbol> {
        val classNamingIndex = GdClassNamingIndex.INSTANCE

        return classNamingIndex.getAllKeys(project)
            .flatMap { classNamingIndex.getGlobally(it, project) }
            .mapNotNull { GdPsiClassSymbolFactory.create(it) }
    }



    override fun getMatchingSymbols(
        qualifiedName: PolySymbolQualifiedName,
        params: PolySymbolNameMatchQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbol> {
        if (qualifiedName.kind != GdPolySymbolKind.CLASS) return emptyList()
        return collectMatchingSymbols(qualifiedName.name)
            .flatMap { it.match(it.name, params, stack) }
    }

    override fun getSymbols(
        kind: PolySymbolKind,
        params: PolySymbolListSymbolsQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbol> {
        if (kind != GdPolySymbolKind.CLASS) return emptyList()
        return collectAllSymbols()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GdPsiClassesPolySymbolScope) return false
        return project == other.project && location == other.location
    }

    override fun hashCode(): Int = 31 * project.hashCode() + (location?.hashCode() ?: 0)
}
