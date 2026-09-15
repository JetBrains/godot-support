package gdscript.library

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.platform.ide.progress.withBackgroundProgress
import com.jetbrains.rider.godot.community.GdScriptProjectLifetimeService
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import gdscript.GdScriptBundle
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.io.path.exists

// TODO delete this class, migrate the same way as the old sdk did in https://jetbrains.team/p/ij/reviews/199208/files
@Service(Service.Level.PROJECT)
class GdLibraryUpdater(private val project: Project) {

    companion object {
        fun getInstance(project: Project): GdLibraryUpdater = project.getService(GdLibraryUpdater::class.java)
    }

    // One load of this project runs at a time, so a second caller of this project waits.
    // The shared core directory has its own application-level lock in GdCoreSdkService, because
    // every open project names the same directory.
    private val loadMutex = Mutex()

    fun scheduleSdkLoad() {
        GdScriptProjectLifetimeService.getInstance(project).scope.launch {
            withBackgroundProgress(project, GdScriptBundle.message("progress.title.check.gdsdk.for.project")) {
                withContext(Dispatchers.IO) {
                    loadSdk()
                }
            }
        }
    }

    private suspend fun loadSdk() {
        // stop if disposed
        if (project.isDisposed) return

        loadMutex.withLock {
            if (project.isDisposed) return

            val projectBasePath = GodotCommunityUtil.getGodotProjectBasePath(project) ?: return
            val godotPath = GodotCommunityUtil.getGodotExecutablePath(project)
            val projectFile = projectBasePath.resolve("project.godot")
            if (!projectFile.exists()) return
            val version = GdSdkUtil.getGodotVersion(project)

            try {
                GdLibraryManager.generateSdkIfNeeded(version, project, godotPath, projectBasePath)
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