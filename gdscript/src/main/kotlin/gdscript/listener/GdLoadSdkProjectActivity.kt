package gdscript.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import gdscript.library.GdLibraryUpdater
import gdscript.polySymbols.GdPolySymbolsConstants
import kotlinx.coroutines.flow.combine
import kotlin.io.path.pathString

/**
 * Loads the SDK for the project.
 * Uses PolySymbols.
 */
class GdLoadSdkProjectActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        if (!GdPolySymbolsConstants.USING_POLY_SYMBOLS)
            return
        if (project.isDisposed) return


        val godotExecutableFlow = GodotCommunityUtil.getGodotExecutablePathFlow(project)
        val basePathFlow = GodotCommunityUtil.getGodotProjectBasePathFlow(project)

        // FIXME by doing something similar to ReferenceGdLibrariesProjectActivity
        /*
        combine(basePathFlow, godotExecutableFlow) emits each time either upstream changes.
        On first startup both may emit in quick succession, calling scheduleSdkLoad twice before the stamp is written by the first call.
        The stamp check in generateSdkIfNeeded reduces but does not eliminate the window (no mutex/synchronization).
        Consider adding a @Volatile boolean guard or Mutex.
         */
        basePathFlow.combine(godotExecutableFlow) { basePath, godotPath ->
            basePath to godotPath
        }.collect { (projectBasePath, godotPath) ->
            if (projectBasePath == null || godotPath == null) return@collect
            GdLibraryUpdater.getInstance(project).scheduleSdkLoad(projectBasePath, godotPath.pathString)
        }
    }
}
