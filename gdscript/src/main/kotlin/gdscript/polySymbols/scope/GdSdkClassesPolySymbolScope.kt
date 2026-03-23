package gdscript.polySymbols.scope

import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.PolySymbolQualifiedName
import com.intellij.polySymbols.completion.PolySymbolCodeCompletionItem
import com.intellij.polySymbols.query.PolySymbolCodeCompletionQueryParams
import com.intellij.polySymbols.query.PolySymbolQueryStack
import com.intellij.polySymbols.utils.PolySymbolScopeWithCache
import gdscript.library.GdDocClassesFoldersService
import gdscript.library.GdSdkFilesProvider
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolNamespace
import gdscript.polySymbols.completion.gdCodeCompletions
import gdscript.polySymbols.sdk.GdSdkClassSymbol

class GdSdkClassesPolySymbolScope(
    project: Project
) : PolySymbolScopeWithCache<Project, Unit>(project, project, Unit) {

    override fun createPointer(): Pointer<out GdSdkClassesPolySymbolScope> = Pointer { GdSdkClassesPolySymbolScope(project) }

    override fun initialize(consumer: (PolySymbol) -> Unit, cacheDependencies: MutableSet<Any>) {
        GdSdkFilesProvider.getInstance(project).getAllSdkFiles().forEach { file ->
            consumer(GdSdkClassSymbol(project, file))
        }

        cacheDependencies.add(GdDocClassesFoldersService.getInstance(project).modificationTracker)
    }

    override fun provides(kind: PolySymbolKind): Boolean =
        kind.namespace == GdPolySymbolNamespace.NAMESPACE && kind == GdPolySymbolKind.CLASS

    override fun getCodeCompletions(
        qualifiedName: PolySymbolQualifiedName,
        params: PolySymbolCodeCompletionQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbolCodeCompletionItem> = gdCodeCompletions(qualifiedName, params, stack)
}
