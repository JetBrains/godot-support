package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.jetbrains.rider.model.godot.frontendBackend.GodotDescriptor
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.pathString

class DotNetGodotMetadataFileWatcher(val project: Project, descriptor: GodotDescriptor) : AsyncFileListener {
    private val mainProjectBasePath: Path = Paths.get(descriptor.mainProjectBasePath)
    // Godot 3 paths
    val godot3Meta: Path = godot3MetaPath(mainProjectBasePath)
    // Godot 4 paths
    val godot4Meta: Path = godot4MetaPath(mainProjectBasePath)

    companion object {
        private const val GODOT3_MONO_META = ".mono/metadata/ide_server_meta.txt"
        private const val GODOT4_MONO_META = ".godot/mono/metadata/ide_messaging_meta.txt"
        fun godot3MetaPath(basePath: Path): Path = basePath.resolve(GODOT3_MONO_META)
        fun godot4MetaPath(basePath: Path): Path = basePath.resolve(GODOT4_MONO_META)
    }

    init {
        val lt = GodotProjectLifetimeService.getLifetime(project)
        for (metaPath in listOf(
            godot3MetaPath(mainProjectBasePath),
            godot4MetaPath(mainProjectBasePath)
        )) {
            LocalFileSystem.getInstance().addRootToWatch(metaPath.parent.pathString, true)
                ?.let { request -> lt.onTermination {
                    LocalFileSystem.getInstance().removeWatchedRoot(request)
                } }
        }
    }

    @Nullable
    override fun prepareChange(@NotNull events: List<@NotNull VFileEvent>): AsyncFileListener.ChangeApplier? {
        // VFS may coalesce creation/deletion events to an ancestor directory,
        // so match both exact file paths and ancestor directory events
        val hasGodot3 = events.any { event -> godot3Meta.startsWith(Path.of(event.path)) }
        val hasGodot4 = events.any { event -> godot4Meta.startsWith(Path.of(event.path)) }

        if (!hasGodot3 && !hasGodot4) return null

        return object : AsyncFileListener.ChangeApplier {
            override fun afterVfsChange() {
                val godotDiscoverer = GodotProjectDiscoverer.getInstance(project)
                if (hasGodot3) {
                    val newPath = DotNetGodotMetadataFileWatcherUtil.getFromMonoMetadataPath(godot3Meta)
                    newPath?.let { godotDiscoverer.godot3Path.set(it) }
                }
                if (hasGodot4) {
                    val newPath = DotNetGodotMetadataFileWatcherUtil.getFromMonoMetadataPath(godot4Meta)
                    newPath?.let { godotDiscoverer.godot4Path.set(it) }
                }
            }
        }
    }
}
