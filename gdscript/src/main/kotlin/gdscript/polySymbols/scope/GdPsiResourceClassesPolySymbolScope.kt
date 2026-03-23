package gdscript.polySymbols.scope

import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
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
import com.intellij.psi.PsiElement
import com.intellij.psi.createSmartPointer
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.impl.GdClassNamingIndex
import gdscript.index.impl.GdFileResIndex
import gdscript.polySymbols.GdClassSymbol
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.completion.gdCodeCompletions
import gdscript.polySymbols.psi.GdPsiClassSymbolFactory
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile
import gdscript.utils.VirtualFileUtil.getPsiFile

/**
 * Project-level scope providing PSI-backed class symbols for user-defined anonymous GDScript classes.
 */
class GdPsiResourceClassesPolySymbolScope(
    private val project: Project,
    private val location: PsiElement? = null,
) : PolySymbolScope {

    override fun createPointer(): Pointer<out PolySymbolScope> {
        val locationPointer = location?.createSmartPointer()
        return Pointer { GdPsiResourceClassesPolySymbolScope(project, locationPointer?.element) }
    }


    private fun collectMatchingSymbols(name: String): List<GdClassSymbol> {
        return GdFileResIndex.getFiles(name.trim('"', '\''), project)
            .mapNotNull { it.getPsiFile(project) as? GdFile }
            .mapNotNull { GdPsiClassSymbolFactory.create(it) }
    }

    private fun collectAllSymbols(): List<PolySymbol> {
        return GdFileResIndex.getNonEmptyKeys(project)
            .flatMap { GdFileResIndex.getFiles(it, project) }
            .mapNotNull { it.getPsiFile(project) as? GdFile }
            .filter { PsiTreeUtil.getStubChildOfType(it, GdClassNaming::class.java) == null }
            .mapNotNull { GdPsiClassSymbolFactory.create(it) }
    }



    override fun getMatchingSymbols(
        qualifiedName: PolySymbolQualifiedName,
        params: PolySymbolNameMatchQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbol> {
        if (qualifiedName.kind != GdPolySymbolKind.RESOURCE_CLASS) return emptyList()
        return collectMatchingSymbols(qualifiedName.name)
            .flatMap { it.match(it.name, params, stack) }
    }

    override fun getSymbols(
        kind: PolySymbolKind,
        params: PolySymbolListSymbolsQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbol> {
        if (kind != GdPolySymbolKind.RESOURCE_CLASS) return emptyList()
        return collectAllSymbols()
    }

    override fun getCodeCompletions(
        qualifiedName: PolySymbolQualifiedName,
        params: PolySymbolCodeCompletionQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbolCodeCompletionItem> = gdCodeCompletions(qualifiedName, params, stack)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GdPsiResourceClassesPolySymbolScope) return false
        return project == other.project && location == other.location
    }

    override fun hashCode(): Int = 31 * project.hashCode() + (location?.hashCode() ?: 0)
}
