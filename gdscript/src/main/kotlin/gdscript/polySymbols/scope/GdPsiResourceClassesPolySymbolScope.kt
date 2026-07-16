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
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.impl.GdFileResIndex
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.psi.GdAliasedNameSymbol
import gdscript.polySymbols.psi.GdPsiClassSymbolFactory
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile
import gdscript.utils.VirtualFileUtil.getPsiFile

/**
 * Project-level scope providing PSI-backed class symbols for user-defined anonymous GDScript classes.
 */
class GdPsiResourceClassesPolySymbolScope(
    private val project: Project,
) : PolySymbolScope {

    override fun createPointer(): Pointer<out PolySymbolScope> =
        Pointer.hardPointer(this)

    private fun collectMatchingSymbols(name: String): List<PolySymbol> =
        GdFileResIndex.getFiles(name.trim('"', '\''), project)
            .asSequence()
            .mapNotNull { it.getPsiFile(project) as? GdFile }
            .mapNotNull { GdPsiClassSymbolFactory.create(it) }
            .map { if (it.name == name) it else GdAliasedNameSymbol(it, name) }
            .toList()

    private fun collectAllSymbols(): List<PolySymbol> =
        GdFileResIndex.getNonEmptyKeys(project)
            .asSequence()
            .flatMap { GdFileResIndex.getFiles(it, project) }
            .mapNotNull { it.getPsiFile(project) as? GdFile }
            .filter { PsiTreeUtil.getStubChildOfType(it, GdClassNaming::class.java) == null }
            .mapNotNull { GdPsiClassSymbolFactory.create(it) }
            .toList()


    override fun getMatchingSymbols(
        qualifiedName: PolySymbolQualifiedName,
        params: PolySymbolNameMatchQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbol> =
        if (qualifiedName.kind != GdPolySymbolKind.RESOURCE_CLASS)
            emptyList()
        else
            collectMatchingSymbols(qualifiedName.name)
                .flatMap { it.match(it.name, params, stack) }

    override fun getSymbols(
        kind: PolySymbolKind,
        params: PolySymbolListSymbolsQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbol> =
        if (kind != GdPolySymbolKind.RESOURCE_CLASS)
            emptyList()
        else
            collectAllSymbols()

    override fun equals(other: Any?): Boolean =
        this === other
            || other is GdPsiResourceClassesPolySymbolScope
            && project == other.project

    override fun hashCode(): Int =
        31 * project.hashCode()
}
