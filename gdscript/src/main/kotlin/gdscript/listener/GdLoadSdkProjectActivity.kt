package gdscript.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import gdscript.library.GdLibraryUpdater
import kotlinx.coroutines.flow.combine

/**
 * Loads the SDK for the project.
 * Uses PolySymbols.
 */
class GdLoadSdkProjectActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        if (project.isDisposed) return

        val godotExecutableFlow = GodotCommunityUtil.getGodotExecutablePathFlow(project)
        val basePathFlow = GodotCommunityUtil.getGodotProjectBasePathFlow(project)

        // Both upstreams may emit in quick succession on startup; the overlapping requests are serialized by
        // GdLibraryUpdater and collapsed by the stamp check in GdLibraryManager.generateSdkIfNeeded().
        basePathFlow.combine(godotExecutableFlow) { basePath, godotPath ->
            basePath to godotPath
        }.collect { (projectBasePath, godotPath) ->
            if (projectBasePath == null || godotPath == null) return@collect
            GdLibraryUpdater.getInstance(project).scheduleSdkLoad(projectBasePath, godotPath)
        }
    }
}
