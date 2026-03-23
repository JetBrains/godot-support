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
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolNamespace
import gdscript.polySymbols.completion.gdCodeCompletions
import gdscript.polySymbols.psi.GdPsiAutoloadSymbol
import gdscript.psi.GdFile
import project.psi.util.ProjectAutoloadUtil

/**
 * Scope that exposes [GdPsiAutoloadSymbol] for all autoload singletons registered in project.godot.
 */
class GdPsiAutoloadScope(
    private val project: Project,
) : PolySymbolScope {

    override fun createPointer(): Pointer<out PolySymbolScope> =
        Pointer { GdPsiAutoloadScope(project) }

    /** Returns all autoloads as [GdPsiAutoloadSymbol] instances. */
    private fun listAutoloads(): List<GdPsiAutoloadSymbol> {
        return ProjectAutoloadUtil.listGlobals(project)
            .filter { it.element is GdFile }
            .map { autoload -> GdPsiAutoloadSymbol(autoload.element as GdFile, autoload.key) }
    }

    override fun getMatchingSymbols(
        qualifiedName: PolySymbolQualifiedName,
        params: PolySymbolNameMatchQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbol> {
        if (qualifiedName.kind.namespace != GdPolySymbolNamespace.NAMESPACE) return emptyList()
        if (qualifiedName.kind != GdPolySymbolKind.AUTOLOAD) return emptyList()
        val name = qualifiedName.name
        return listAutoloads()
            .filter { it.name == name }
            .flatMap { it.match(name, params, stack) }
    }

    override fun getSymbols(
        kind: PolySymbolKind,
        params: PolySymbolListSymbolsQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbol> {
        if (kind.namespace != GdPolySymbolNamespace.NAMESPACE) return emptyList()
        if (kind != GdPolySymbolKind.AUTOLOAD) return emptyList()
        return listAutoloads()
    }

    override fun getCodeCompletions(
        qualifiedName: PolySymbolQualifiedName,
        params: PolySymbolCodeCompletionQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbolCodeCompletionItem> = gdCodeCompletions(qualifiedName, params, stack)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GdPsiAutoloadScope) return false
        return project == other.project
    }

    override fun hashCode(): Int = project.hashCode()
}
