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
import gdscript.library.GdSdkFilesProvider
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolNamespace
import gdscript.polySymbols.GdPolySymbolsConstants
import gdscript.polySymbols.completion.gdCodeCompletions
import gdscript.polySymbols.sdk.GdSdkConstantSymbol
import gdscript.polySymbols.sdk.GdSdkConstructorSymbol
import gdscript.polySymbols.sdk.GdSdkEnumSymbol
import gdscript.polySymbols.sdk.GdSdkMethodSymbol
import gdscript.polySymbols.sdk.GdSdkPropertySymbol
import gdscript.polySymbols.sdk.GdSdkSignalSymbol
import gdscript.polySymbols.sdk.xml.GdSdkXmlParser

/**
 * Scope for global symbols inherent to the language defined in GDScript core SDK files.
 */
class GdSdkGlobalPolySymbolScope(
    project: Project
) : PolySymbolScopeWithCache<Project, Unit>(project, project, Unit) {

    override fun createPointer(): Pointer<out GdSdkGlobalPolySymbolScope> = Pointer { GdSdkGlobalPolySymbolScope(project) }

    private val modificationsTracker = GdSdkSymbolsModificationTracker.getInstance(project)

    override fun initialize(consumer: (PolySymbol) -> Unit, cacheDependencies: MutableSet<Any>) {
        // TODO optimize this, dont look through all files, only GDSCRIPT_GLOBAL_SCOPE_FILE_NAMES
        GdSdkFilesProvider.getInstance(project).getAllCoreSdkFiles().forEach { file ->
            if (GdPolySymbolsConstants.GLOBAL_CLASSES.contains(file.nameWithoutExtension)) {
                val classData = GdSdkXmlParser.parseClass(file) ?: return@forEach
                classData.constructors.forEach { cDoc ->
                    consumer(GdSdkConstructorSymbol(project, classData.name, cDoc))
                }
                classData.methods.forEach { mDoc ->
                    consumer(GdSdkMethodSymbol(project, classData.name, mDoc))
                }
                classData.properties.forEach { pDoc ->
                    consumer(GdSdkPropertySymbol(project, classData.name, pDoc))
                }
                classData.constants.forEach { cDoc ->
                    consumer(GdSdkConstantSymbol(project, classData.name, cDoc))
                }
                classData.enums.forEach { eDoc ->
                    consumer(GdSdkEnumSymbol(project, classData.name, eDoc))
                }
                classData.signals.forEach { sDoc ->
                    consumer(GdSdkSignalSymbol(project, classData.name, sDoc))
                }
            }
        }

        cacheDependencies.add(modificationsTracker)
    }

    override fun provides(kind: PolySymbolKind): Boolean =
        kind.namespace == GdPolySymbolNamespace.NAMESPACE && kind in setOf(
            GdPolySymbolKind.METHOD,
            GdPolySymbolKind.PROPERTY,
            GdPolySymbolKind.CONSTANT,
            GdPolySymbolKind.ENUM,
            GdPolySymbolKind.SIGNAL,
        )

    override fun getCodeCompletions(
        qualifiedName: PolySymbolQualifiedName,
        params: PolySymbolCodeCompletionQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbolCodeCompletionItem> = gdCodeCompletions(qualifiedName, params, stack)
}
