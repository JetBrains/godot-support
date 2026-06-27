package gdscript.library

import com.intellij.openapi.application.smartReadAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.createNestedDisposable
import com.intellij.openapi.util.io.toNioPathOrNull
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.events.VFileCreateEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.openapi.vfs.newvfs.events.VFilePropertyChangeEvent
import com.intellij.openapi.vfs.toNioPathOrNull
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.ProjectScope
import com.jetbrains.rd.util.lifetime.SequentialLifetimes
import com.jetbrains.rd.util.lifetime.isAlive
import com.jetbrains.rider.godot.community.GdScriptProjectLifetimeService
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

@Service(Service.Level.PROJECT)
class GdExtensionWatchService(private val project: Project) {

    data class Snapshot(
        val gdextensions: List<Path>,
        val libraries: List<Path>,
        val fingerprint: Long,
    ) {
        companion object {
            val EMPTY: Snapshot = Snapshot(emptyList(), emptyList(), 0L)
        }
    }

    private val _extensionsFlow = MutableStateFlow(Snapshot.EMPTY)
    val extensionsFlow: StateFlow<Snapshot> = _extensionsFlow.asStateFlow()

    private val sequentialLifetimes = SequentialLifetimes(GdScriptProjectLifetimeService.getLifetime(project))

    init {
        GdScriptProjectLifetimeService.getScope(project).launch {
            GodotCommunityUtil.getGodotProjectBasePathFlow(project).collect { basePath ->
                if (basePath == null) {
                    thisLogger().trace("base path cleared")
                    _extensionsFlow.value = Snapshot.EMPTY
                    return@collect
                }
                thisLogger().trace("base path changed: $basePath")
                watch(basePath)
            }
        }
    }

    private suspend fun watch(basePath: Path) {
        val watchLifetime = sequentialLifetimes.next().lifetime
        val initial = smartReadAction(project) { scan(basePath) }
        thisLogger().trace(
            "watching $basePath; initial scan: ${initial.gdextensions.size} gdextensions, " +
            "${initial.libraries.size} libraries"
        )
        _extensionsFlow.value = initial

        val listener = AsyncFileListener { events ->
            val current = _extensionsFlow.value
            val libsSet = current.libraries.toHashSet()
            var gdextensionTouched = false
            var libraryTouched = false
            for (event in events) {
                val info = describeEvent(event) ?: continue
                // VFileCreateEvent for a directory does not produce child events for files already inside it
                if (info.name.endsWith(".gdextension") || info.isDirectory) {
                    gdextensionTouched = true
                    break
                } else if (libsSet.contains(info.nioPath)) {
                    libraryTouched = true
                    break
                }
            }
            if (!gdextensionTouched && !libraryTouched) null
            else object : AsyncFileListener.ChangeApplier {
                override fun afterVfsChange() {
                    thisLogger().trace(
                        "VFS change relevant (gdextension=$gdextensionTouched library=$libraryTouched); rescanning"
                    )
                    GdScriptProjectLifetimeService.getScope(project).launch {
                        if (!watchLifetime.isAlive) return@launch
                        val snapshot = smartReadAction(project) { scan(basePath) }
                        if (!watchLifetime.isAlive) return@launch
                        _extensionsFlow.value = snapshot
                    }
                }
            }
        }
        VirtualFileManager.getInstance().addAsyncFileListener(listener, watchLifetime.createNestedDisposable())
    }

    private data class EventInfo(val name: String, val nioPath: Path, val isDirectory: Boolean)

    private fun describeEvent(event: VFileEvent): EventInfo? = when (event) {
        is VFileCreateEvent -> {
            if (!event.parent.isInLocalFileSystem) null
            else event.path.toNioPathOrNull()
                ?.let { EventInfo(event.childName, it, event.isDirectory) }
        }
        else -> {
            val file = event.file
            if (file == null || !file.isInLocalFileSystem) null
            else file.toNioPathOrNull()?.let { nioPath ->
                // On PROP_NAME change, file.name is still the OLD name during prepareChange;
                val name = (event as? VFilePropertyChangeEvent)
                    ?.takeIf { it.propertyName == VirtualFile.PROP_NAME }
                    ?.newValue?.toString()
                    ?: file.name
                EventInfo(name, nioPath, file.isDirectory)
            }
        }
    }

    private fun scan(basePath: Path): Snapshot {
        if (!basePath.exists() || !basePath.isDirectory()) return Snapshot.EMPTY
        val gdextensions = FilenameIndex.getAllFilesByExt(project, "gdextension", ProjectScope.getContentScope(project))
            .asSequence()
            .mapNotNull { it.toNioPathOrNull() }
            .filter { it.startsWith(basePath) }
            .distinct()
            .toList()
        val libraries = gdextensions.flatMap { GdExtensionFile.referencedLibraries(it) }.distinct()
        val fingerprint = libraries.fold(0L) { acc, lib ->
            val (size, mtime) = try {
                val attrs = Files.readAttributes(lib, BasicFileAttributes::class.java)
                attrs.size() to attrs.lastModifiedTime().toMillis()
            } catch (_: IOException) {
                -1L to -1L
            }
            (acc * 1_000_003L) xor size xor mtime
        }
        thisLogger().trace(
            "scanned $basePath: ${gdextensions.size} gdextensions, ${libraries.size} libraries, fingerprint=$fingerprint"
        )
        return Snapshot(gdextensions, libraries, fingerprint)
    }

    companion object {
        fun getInstance(project: Project): GdExtensionWatchService = project.service()
    }
}
