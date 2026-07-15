package gdscript.polySymbols.scope

import com.intellij.openapi.project.Project
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.query.polySymbolScopeCached
import gdscript.library.GdDocClassesFoldersService
import gdscript.library.GdSdkFilesProvider
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.sdk.GdSdkClassSymbol

fun gdSdkClassesPolySymbolScope(project: Project): PolySymbolScope =
    polySymbolScopeCached(project) {
        provides(GdPolySymbolKind.CLASS)
        initialize {
            cacheDependencies(GdDocClassesFoldersService.getInstance(project).modificationTracker)
            GdSdkFilesProvider.getInstance(project).getAllSdkFiles().forEach { file ->
                add(GdSdkClassSymbol(project, file))
            }
        }
    }
