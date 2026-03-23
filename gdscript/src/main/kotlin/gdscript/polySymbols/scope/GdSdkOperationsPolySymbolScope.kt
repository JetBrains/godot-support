package gdscript.polySymbols.scope

import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.utils.PolySymbolScopeWithCache
import gdscript.library.GdSdkFilesProvider
import gdscript.library.GdSdkUtil
import gdscript.polySymbols.config.GdOperatorSymbol
import gdscript.polySymbols.GdPolySymbolNamespace
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.sdk.xml.GdSdkXmlParser

class GdSdkOperationsPolySymbolScope(
    project: Project
) : PolySymbolScopeWithCache<Project, Unit>(project, project, Unit) {

    private val modificationsTracker = GdSdkSymbolsModificationTracker.getInstance(project)

    override fun createPointer(): Pointer<out GdSdkOperationsPolySymbolScope> = Pointer { GdSdkOperationsPolySymbolScope(project) }

    override fun initialize(consumer: (PolySymbol) -> Unit, cacheDependencies: MutableSet<Any>) {
        // TODO also cache this, probably cache what classes have operations and make each symbol parse and cache its own data like in classes
        // Only Core SDK files can declare operations. We can probably run it once per version to see which have operations and cache the file names.
        GdSdkFilesProvider.getInstance(project).getAllCoreSdkFiles().forEach { file ->
            val operations = GdSdkXmlParser.parseOperations(file) ?: return@forEach
            operations.operators.forEach { opData ->
                consumer(GdOperatorSymbol(project, operations.left, opData))
            }
        }

        cacheDependencies.add(modificationsTracker)
    }

    override fun provides(kind: PolySymbolKind): Boolean =
        kind.namespace == GdPolySymbolNamespace.NAMESPACE && kind == GdPolySymbolKind.OPERATOR
}
