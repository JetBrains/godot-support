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
import gdscript.library.GdSdkUtil
import gdscript.polySymbols.config.GdAnnotationSymbol
import gdscript.polySymbols.GdPolySymbolNamespace
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolsConstants
import gdscript.polySymbols.completion.gdCodeCompletions
import gdscript.polySymbols.sdk.xml.GdSdkXmlParser
import kotlin.io.path.nameWithoutExtension

class GdSdkAnnotationsPolySymbolScope(
    project: Project
) : PolySymbolScopeWithCache<Project, Unit>(project, project, Unit) {

    private val modificationsTracker = GdSdkSymbolsModificationTracker.getInstance(project)

    override fun createPointer(): Pointer<out GdSdkAnnotationsPolySymbolScope> = Pointer { GdSdkAnnotationsPolySymbolScope(project) }

    override fun initialize(consumer: (PolySymbol) -> Unit, cacheDependencies: MutableSet<Any>) {
        // TODO optimize this, dont look through all files, only ANNOTATIONS_FILE_NAME
        GdSdkFilesProvider.getInstance(project).getAllCoreSdkFiles().forEach { file ->
            if (file.nameWithoutExtension == GdPolySymbolsConstants.ANNOTATIONS_FILE_NAME) {
                val annotations = GdSdkXmlParser.parseAnnotations(file) ?: return@forEach
                annotations.forEach { aData ->
                    consumer(GdAnnotationSymbol(project, GdPolySymbolsConstants.ANNOTATIONS_FILE_NAME, aData.name, aData))
                }
            }
        }

        cacheDependencies.add(modificationsTracker)
    }

    override fun provides(kind: PolySymbolKind): Boolean =
        kind.namespace == GdPolySymbolNamespace.NAMESPACE && kind == GdPolySymbolKind.ANNOTATION

    override fun getCodeCompletions(
        qualifiedName: PolySymbolQualifiedName,
        params: PolySymbolCodeCompletionQueryParams,
        stack: PolySymbolQueryStack,
    ): List<PolySymbolCodeCompletionItem> = gdCodeCompletions(qualifiedName, params, stack)
}
