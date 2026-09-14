package gdscript.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import gdscript.library.GdLibraryUpdater
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Loads the SDK for the project.
 * Uses PolySymbols.
 */
class GdLoadSdkProjectActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        if (project.isDisposed) return

        val godotExecutableFlow = GodotCommunityUtil.getGodotExecutablePathFlow(project)
        val basePathFlow = GodotCommunityUtil.getGodotProjectBasePathFlow(project)

        // FIXME by doing something similar to ReferenceGdLibrariesProjectActivity
        /*
        combine(basePathFlow, godotExecutableFlow) emits each time either upstream changes.
        On first startup both can emit in quick succession, so the same pair arrives twice.
        distinctUntilChanged() drops the repeat. GdLibraryUpdater serializes the remaining loads with a Mutex.
         */
        basePathFlow.combine(godotExecutableFlow) { basePath, godotPath ->
            basePath to godotPath
        }.distinctUntilChanged().collect { (projectBasePath, godotPath) ->
            if (projectBasePath == null || godotPath == null) return@collect
            GdLibraryUpdater.getInstance(project).scheduleSdkLoad(projectBasePath, godotPath)
        }
    }
}
