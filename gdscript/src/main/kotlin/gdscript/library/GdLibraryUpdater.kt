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
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.exists

// TODO delete this class, migrate the same way as the old sdk did in https://jetbrains.team/p/ij/reviews/199208/files
// Use ReferenceGdLibrariesProjectActivity to get the version
@Service(Service.Level.PROJECT)
class GdLibraryUpdater(private val project: Project) {

    companion object {
        fun getInstance(project: Project): GdLibraryUpdater = project.getService(GdLibraryUpdater::class.java)
    }

    // One load of this project runs at a time, so a second caller of this project waits.
    // The shared core directory has its own application-level lock in GdCoreSdkService, because
    // every open project names the same directory.
    private val loadMutex = Mutex()

    // Orders the requests of this project. A load stops only when a newer load already finished.
    private val loadRequests = GdSdkLoadRequests()

    fun scheduleSdkLoad(projectBasePath: Path, godotPath: Path) {
        val token = loadRequests.newRequest()
        GdScriptProjectLifetimeService.getInstance(project).scope.launch {
            withBackgroundProgress(project, GdScriptBundle.message("progress.title.check.gdsdk.for.project")) {
                withContext(Dispatchers.IO) {
                    loadSdk(projectBasePath, godotPath, token)
                }
            }
        }
    }

    private suspend fun loadSdk(projectBasePath: Path, godotPath: Path, token: Long) {
        val projectFile = projectBasePath.resolve("project.godot")
        if (!projectFile.exists()) return
        val version = GdSdkUtil.getGodotVersion(projectFile) ?: return

        // stop if disposed
        if (project.isDisposed) return

        loadMutex.withLock {
            if (loadRequests.isSuperseded(token)) {
                thisLogger().info("A newer SDK load already finished, so the load for $godotPath stops.")
                return
            }
            if (project.isDisposed) return

            try {
                GdLibraryManager.generateSdkIfNeeded(version, project, godotPath, projectBasePath)
                loadRequests.finished(token)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                // A file system failure is routine, so the next request can retry.
                thisLogger().warn("Failed to load the SDK because of an input or output failure.", e)
            } catch (e: Exception) {
                thisLogger().error("Failed to load SDK from XML", e)
            }
        }
    }
}