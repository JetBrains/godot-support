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
            // getAllSdkFiles() includes the core SDK, which becomes visible only after the doctool
            // output is refreshed into the VFS, so the core SDK tracker is a dependency too.
            cacheDependencies(
                GdDocClassesFoldersService.getInstance(project).modificationTracker,
                GdSdkSymbolsModificationTracker.getInstance(project),
            )
            GdSdkFilesProvider.getInstance(project).getAllSdkFiles().forEach { file ->
                add(GdSdkClassSymbol(project, file))
            }
        }
    }
