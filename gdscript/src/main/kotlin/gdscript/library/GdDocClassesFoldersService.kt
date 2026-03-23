package gdscript.library

import com.intellij.openapi.application.readAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.createNestedDisposable
import com.intellij.openapi.util.ModificationTracker
import com.intellij.openapi.util.SimpleModificationTracker
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.jetbrains.rider.godot.community.GdScriptProjectLifetimeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Tracks `doc_classes` folders anywhere in the project (including under `.idea/`).
 * These folders are used to locate GDExtension XML files.
 *
 * Symbol providers read [currentFolders] to list SDK XML sources, and use
 * [modificationTracker] as a cache dependency so SDK caches invalidate when the folder
 * set, the XML file set, or the content of any tracked XML changes.
 */
@Service(Service.Level.PROJECT)
class GdDocClassesFoldersService(
    private val project: Project,
    private val scope: CoroutineScope,
) {

    companion object {
        private const val DOC_CLASSES_NAME = "doc_classes"

        fun getInstance(project: Project): GdDocClassesFoldersService = project.service()
    }

    private val tracker = SimpleModificationTracker()
    val modificationTracker: ModificationTracker get() = tracker

    private val foldersFlow = MutableStateFlow<Set<VirtualFile>>(emptySet())

    fun currentFolders(): Set<VirtualFile> = foldersFlow.value

    init {
        VirtualFileManager.getInstance().addAsyncFileListener(
            DocClassesFileListener(),
            GdScriptProjectLifetimeService.getLifetime(project).createNestedDisposable()
        )

        scope.launch(Dispatchers.IO) {
            scheduleRescan()
        }
    }

    fun scheduleRescan() {
        scope.launch(Dispatchers.IO) {
            DumbService.getInstance(project).waitForSmartMode()
            val newFolders = readAction { discoverFolders() }
            if (newFolders != foldersFlow.value) {
                foldersFlow.value = newFolders
                tracker.incModificationCount()
            }
        }
    }

    private fun discoverFolders(): Set<VirtualFile> {
        return FilenameIndex.getVirtualFilesByName(
            DOC_CLASSES_NAME, true,
            GlobalSearchScope.allScope(project)
        ).filter { it.isDirectory && it.isValid }
            .toSet()
    }

    private inner class DocClassesFileListener : AsyncFileListener {
        override fun prepareChange(events: MutableList<out VFileEvent>): AsyncFileListener.ChangeApplier? {
            val current = foldersFlow.value
            val matches = events.any { matchesDocClasses(it, current) }
            if (!matches) return null
            return object : AsyncFileListener.ChangeApplier {
                override fun afterVfsChange() {
                    tracker.incModificationCount()
                    scheduleRescan()
                }
            }
        }
    }

    private fun matchesDocClasses(event: VFileEvent, current: Set<VirtualFile>): Boolean {
        val path = event.path
        if (path.endsWith("/$DOC_CLASSES_NAME") || "/$DOC_CLASSES_NAME/" in path) return true

        return current.any { folder ->
            folder.isValid && folder.path.startsWith(path)
        }
    }
}
