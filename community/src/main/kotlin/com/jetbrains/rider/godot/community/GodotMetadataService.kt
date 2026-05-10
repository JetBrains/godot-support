package com.jetbrains.rider.godot.community

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.createNestedDisposable
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFileManager
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.lifetime.LifetimeDefinition
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

    private val lifetimeDefinition = LifetimeDefinition()
    val serviceLifetime: Lifetime get() = lifetimeDefinition.lifetime

    fun watchBasePath(basePath: Path, lt: Lifetime) {
        lt.onTermination { _executablePathFlow.value = null }

        val watchDir = basePath.resolve(GODOT_EDITOR_CONFIG_DIR)
        LocalFileSystem.getInstance().addRootToWatch(watchDir.pathString, true)
            ?.let { request -> lt.onTermination { LocalFileSystem.getInstance().removeWatchedRoot(request) } }

        addFileWatcher(basePath.resolve(METADATA_REL_PATH), lt)
        addFileWatcher(basePath.resolve(RIDER_ADDON_REL_PATH), lt)
    }

    private fun addFileWatcher(path: Path, lt: Lifetime) {
        readExecutablePathFromFile(path)?.let { _executablePathFlow.value = it }
        VirtualFileManager.getInstance().addAsyncFileListener(
            GodotMetadataFileWatcher(path) {
                thisLogger().info("${path.fileName} changed, updating executablePathFlow")
                readExecutablePathFromFile(path)?.let { _executablePathFlow.value = it }
            },
            lt.createNestedDisposable()
        )
    }

    override fun dispose() {
        lifetimeDefinition.terminate()
    }

    companion object {
        private const val GODOT_EDITOR_CONFIG_DIR: String = ".godot/editor"
        private const val METADATA_REL_PATH: String = "$GODOT_EDITOR_CONFIG_DIR/project_metadata.cfg"
        private const val RIDER_ADDON_REL_PATH: String = "$GODOT_EDITOR_CONFIG_DIR/rider_addon_godot_editor.cfg"

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

        fun readGodot4ExecutablePath(projectPath: Path): Path? =
            readExecutablePathFromFile(projectPath.resolve(METADATA_REL_PATH))
    }
}
