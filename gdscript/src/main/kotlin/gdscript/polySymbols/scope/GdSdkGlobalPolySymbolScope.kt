package gdscript.polySymbols.scope

import com.intellij.openapi.project.Project
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.query.polySymbolScopeCached
import gdscript.library.GdSdkFilesProvider
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolsConstants
import gdscript.polySymbols.sdk.GdSdkConstantSymbol
import gdscript.polySymbols.sdk.GdSdkEnumSymbol
import gdscript.polySymbols.sdk.GdSdkMethodSymbol
import gdscript.polySymbols.sdk.GdSdkPropertySymbol
import gdscript.polySymbols.sdk.GdSdkSignalSymbol
import gdscript.polySymbols.sdk.xml.GdSdkXmlParser

/**
 * Scope for global symbols inherent to the language defined in GDScript core SDK files.
 */
fun gdSdkGlobalPolySymbolScope(project: Project): PolySymbolScope =
    polySymbolScopeCached(project) {
        provides(
            GdPolySymbolKind.METHOD,
            GdPolySymbolKind.PROPERTY,
            GdPolySymbolKind.CONSTANT,
            GdPolySymbolKind.ENUM,
            GdPolySymbolKind.SIGNAL,
        )
        initialize {
            cacheDependencies(GdSdkSymbolsModificationTracker.getInstance(project))

            // TODO optimize this, dont look through all files, only GDSCRIPT_GLOBAL_SCOPE_FILE_NAMES
            GdSdkFilesProvider.getInstance(project).getAllCoreSdkFiles().forEach { file ->
                if (GdPolySymbolsConstants.GLOBAL_CLASSES.contains(file.nameWithoutExtension)) {
                    val classData = GdSdkXmlParser.parseClass(file) ?: return@forEach
                    // classData.constructors is always empty for GLOBAL_CLASSES files (global/utility
                    // namespaces aren't instantiable) — confirmed empirically, not added here.
                    classData.methods.forEach { mDoc ->
                        add(GdSdkMethodSymbol(project, classData.name, mDoc))
                    }
                    classData.properties.forEach { pDoc ->
                        add(GdSdkPropertySymbol(project, classData.name, pDoc))
                    }
                    classData.constants.forEach { cDoc ->
                        add(GdSdkConstantSymbol(project, classData.name, cDoc))
                    }
                    classData.enums.forEach { eDoc ->
                        add(GdSdkEnumSymbol(project, classData.name, eDoc))
                    }
                    classData.signals.forEach { sDoc ->
                        add(GdSdkSignalSymbol(project, classData.name, sDoc))
                    }
                }
            }
        }
    }
