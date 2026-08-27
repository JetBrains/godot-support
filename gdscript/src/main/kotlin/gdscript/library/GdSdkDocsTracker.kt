package gdscript.library

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.createNestedDisposable
import com.intellij.openapi.util.ModificationTracker
import com.intellij.openapi.util.SimpleModificationTracker
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.jetbrains.rider.godot.community.GdScriptProjectLifetimeService
import java.nio.file.Path

/**
 * Single source of truth for "the SDK documentation changed": watches the folders we generate the XMLs into (see
 * [GdSdkPathManager]) and serves as the cache dependency of everything that parses them.
 *
 * The generated XMLs live outside the project content (the core SDK is in the plugin directory, the project ones are
 * project-store files), so the platform reports no PSI change for them and the daemon keeps the stale highlighting:
 * both the cache invalidation and the restart have to be done explicitly, which is what [docsChanged] does.
 */
@Service(Service.Level.PROJECT)
class GdSdkDocsTracker(private val project: Project) : ModificationTracker {

    companion object {
        fun getInstance(project: Project): GdSdkDocsTracker = project.service()
    }

    private val tracker = SimpleModificationTracker()

    override fun getModificationCount(): Long = tracker.modificationCount

    init {
        VirtualFileManager.getInstance().addAsyncFileListener(
            DocsFileListener(),
            GdScriptProjectLifetimeService.getLifetime(project).createNestedDisposable()
        )
    }

    /**
     * Invalidates the parsed SDK data and re-highlights the open editors. Called after our own generation and whenever
     * the documentation changes behind the plugin's back, e.g. the docs being deleted or regenerated externally.
     */
    fun docsChanged() {
        tracker.incModificationCount()
        DaemonCodeAnalyzer.getInstance(project).restart("GdScript SDK documentation changed")
    }

    private fun watchedRoots(): List<Path> {
        return GdSdkPathManager.getProjectDocDirs(project) + listOf(GdSdkPathManager.getCoreSdkDocsRoot())
    }

    private fun isDocsChange(event: VFileEvent, roots: List<Path>): Boolean {
        val path = event.path
        // A root being removed together with one of its parents matters as much as a change inside it, hence both
        // directions of the ancestor check.
        return roots.any { root ->
            val rootPath = root.toString()
            FileUtil.isAncestor(rootPath, path, false) || FileUtil.isAncestor(path, rootPath, false)
        }
    }

    private inner class DocsFileListener : AsyncFileListener {
        override fun prepareChange(events: MutableList<out VFileEvent>): AsyncFileListener.ChangeApplier? {
            val roots = watchedRoots()
            if (events.none { isDocsChange(it, roots) }) return null

            return object : AsyncFileListener.ChangeApplier {
                override fun afterVfsChange() {
                    if (project.isDisposed) return
                    docsChanged()
                }
            }
        }
    }
}
