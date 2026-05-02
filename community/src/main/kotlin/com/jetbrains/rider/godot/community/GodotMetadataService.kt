package com.jetbrains.rider.godot.community

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFileManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString
import kotlin.io.path.readLines

@Service(Service.Level.PROJECT)
class GodotMetadataService : Disposable {
    private val _executablePathFlow = MutableStateFlow<Path?>(null)
    val executablePathFlow: StateFlow<Path?> = _executablePathFlow.asStateFlow()

    private var currentWatcher: GodotMetadataFileWatcher? = null
    private var watcherDisposable: Disposable? = null
    private var watchedRoot: LocalFileSystem.WatchRequest? = null

    fun startWatcher(basePath: Path) {
        stopWatcher()
        thisLogger().info("project_metadata.cfg startWatcher: watching $basePath")
        // Register parent of project_metadata.cfg with IntelliJ's file system watcher so VFS receives events
        // even when the directory is not part of the project model (e.g. CMake projects)
        val metadataPath = basePath.resolve(GodotMetadataFileWatcher.METADATA_REL_PATH)
        watchedRoot = LocalFileSystem.getInstance().addRootToWatch(
            metadataPath.parent.pathString, true
        )

        _executablePathFlow.value = readExecutablePathFromFile(metadataPath)

        val disposable = Disposer.newDisposable(this, "GodotMetadataWatcher")
        val watcher = GodotMetadataFileWatcher(basePath) { changedPath ->
            thisLogger().info("project_metadata.cfg onMetadataChanged: updating executablePathFlow for $changedPath")
            _executablePathFlow.value = readExecutablePathFromFile(changedPath)
        }
        currentWatcher = watcher
        watcherDisposable = disposable
        VirtualFileManager.getInstance().addAsyncFileListener(watcher, disposable)
    }

    fun stopWatcher() {
        watchedRoot?.let { LocalFileSystem.getInstance().removeWatchedRoot(it) }
        watchedRoot = null
        watcherDisposable?.let { Disposer.dispose(it) }
        watcherDisposable = null
        currentWatcher = null
        _executablePathFlow.value = null
    }

    override fun dispose() {
        stopWatcher()
    }

    companion object {
        fun getInstance(project: Project): GodotMetadataService = project.service()

        fun readExecutablePathFromFile(metadataPath: Path): Path? {
            if (!metadataPath.exists()) return null
            return metadataPath.readLines()
                .firstOrNull { it.startsWith("executable_path=") }
                ?.substringAfter("executable_path=")
                ?.trim('"')
                ?.let { Path.of(it) }
                ?.takeIf { it.exists() }
        }
    }
}
