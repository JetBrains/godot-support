package com.jetbrains.rider.godot.community

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFileManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.nio.file.Path

data class MetadataChangeEvent(
    val metadataPath: Path,
    val timestamp: Long = System.currentTimeMillis()
)

@Service(Service.Level.PROJECT)
class GodotMetadataService : Disposable {
    private val _metadataChangeFlow = MutableStateFlow<MetadataChangeEvent?>(null)
    val metadataChangeFlow: StateFlow<MetadataChangeEvent?> = _metadataChangeFlow.asStateFlow()

    private var currentWatcher: GodotMetadataFileWatcher? = null
    private var watcherDisposable: Disposable? = null

    fun startWatcher(basePath: Path) {
        stopWatcher()
        val disposable = Disposer.newDisposable(this, "GodotMetadataWatcher")
        val watcher = GodotMetadataFileWatcher(basePath) { metadataPath ->
            _metadataChangeFlow.value = MetadataChangeEvent(metadataPath)
        }
        currentWatcher = watcher
        watcherDisposable = disposable
        VirtualFileManager.getInstance().addAsyncFileListener(watcher, disposable)
    }

    fun stopWatcher() {
        watcherDisposable?.let {Disposer.dispose(it)}
        watcherDisposable = null
        currentWatcher = null
    }

    override fun dispose() {
        stopWatcher()
    }

    companion object {
        fun getInstance(project: Project): GodotMetadataService = project.service()
    }
}
