package gdscript.polySymbols.scope

import com.intellij.openapi.project.Project
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.query.polySymbolScopeCached
import gdscript.library.GdSdkDocsTracker
import gdscript.library.GdSdkFilesProvider
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.sdk.GdSdkClassSymbol

fun gdSdkClassesPolySymbolScope(project: Project): PolySymbolScope =
    polySymbolScopeCached(project) {
        provides(GdPolySymbolKind.CLASS)
        initialize {
            // All the SDK files are generated, and become visible only after the doctool output is refreshed into
            // the VFS, which is exactly what the service reports.
            cacheDependencies(GdSdkDocsTracker.getInstance(project))
            GdSdkFilesProvider.getInstance(project).getAllSdkFiles().forEach { file ->
                add(GdSdkClassSymbol(project, file))
            }
        }
    }
