package gdscript.library

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.platform.ide.progress.withBackgroundProgress
import com.jetbrains.rider.godot.community.GdScriptProjectLifetimeService
import gdscript.GdScriptBundle
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.nio.file.Path
import kotlin.io.path.exists

// TODO delete this class, migrate the same way as the old sdk did in https://jetbrains.team/p/ij/reviews/199208/files
// Use ReferenceGdLibrariesProjectActivity to get the version
@Service(Service.Level.PROJECT)
class GdLibraryUpdater(private val project: Project) {

    companion object {
        fun getInstance(project: Project): GdLibraryUpdater = project.getService(GdLibraryUpdater::class.java)
    }

    /**
     * A generation spawns Godot processes and rewrites the doc directories, so two of them must not overlap: the
     * requests are triggered from unrelated places (the startup activity and the GDExtension watcher) and are simply
     * serialized here.
     */
    private val generationMutex = Mutex()

    fun scheduleSdkLoad(projectBasePath: Path, godotPath: Path) {
        GdScriptProjectLifetimeService.getInstance(project).scope.launch {
            withBackgroundProgress(project, GdScriptBundle.message("progress.title.check.gdsdk.for.project")) {
                withContext(Dispatchers.IO) {
                    loadSdk(projectBasePath, godotPath)
                }
            }
        }
    }

    private suspend fun loadSdk(projectBasePath: Path, godotPath: Path) {
        val projectFile = projectBasePath.resolve("project.godot")
        if (!projectFile.exists()) return
        val version = GdSdkUtil.getGodotVersion(projectFile) ?: return

        // stop if disposed
        if (project.isDisposed) return

        try {
            generationMutex.withLock {
                if (project.isDisposed) return
                GdLibraryManager.generateSdkIfNeeded(version, project, godotPath, projectBasePath)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            thisLogger().error("Failed to load SDK from XML", e)
        }
    }
}