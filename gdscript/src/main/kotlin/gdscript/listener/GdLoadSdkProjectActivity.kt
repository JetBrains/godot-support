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

        // A missing version does not stop a load. The updater reads the current paths after it acquires the mutex.
        combine(basePathFlow, godotExecutableFlow, projectInfoFlow) { basePath, godotPath, info ->
            if (basePath != null) Triple(basePath, godotPath, info?.version) else null
        }.distinctUntilChanged().collect { selection ->
            if (selection == null) return@collect
            GdLibraryUpdater.getInstance(project).scheduleSdkLoad()
        }
    }
}
