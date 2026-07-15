package gdscript.polySymbols.scope

import com.intellij.openapi.project.Project
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.query.polySymbolScopeCached
import gdscript.library.GdSdkFilesProvider
import gdscript.polySymbols.config.GdAnnotationSymbol
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolsConstants
import gdscript.polySymbols.sdk.xml.GdSdkXmlParser

fun gdSdkAnnotationsPolySymbolScope(project: Project): PolySymbolScope =
    polySymbolScopeCached(project) {
        provides(GdPolySymbolKind.ANNOTATION)
        initialize {
            cacheDependencies(GdSdkSymbolsModificationTracker.getInstance(project))

            // TODO optimize this, dont look through all files, only ANNOTATIONS_FILE_NAME
            GdSdkFilesProvider.getInstance(project).getAllCoreSdkFiles().forEach { file ->
                if (file.nameWithoutExtension == GdPolySymbolsConstants.ANNOTATIONS_FILE_NAME) {
                    val annotations = GdSdkXmlParser.parseAnnotations(file) ?: return@forEach
                    annotations.forEach { aData ->
                        add(GdAnnotationSymbol(project, GdPolySymbolsConstants.ANNOTATIONS_FILE_NAME, aData.name, aData))
                    }
                }
            }
        }
    }
