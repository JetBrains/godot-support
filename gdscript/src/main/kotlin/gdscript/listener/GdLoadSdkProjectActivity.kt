package gdscript.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jetbrains.rider.godot.community.GdProjectGodotService
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
        val projectInfoFlow = GdProjectGodotService.getInstance(project).projectInfoFlow

        // The updater reads the current paths and the version after it acquires the mutex.
        // It skips the load when the version is not known.
        combine(basePathFlow, godotExecutableFlow, projectInfoFlow) { basePath, godotPath, info ->
            if (basePath != null) Triple(basePath, godotPath, info?.version) else null
        }.distinctUntilChanged().collect { selection ->
            if (selection == null) return@collect
            GdLibraryUpdater.getInstance(project).scheduleSdkLoad()
        }
    }
}
