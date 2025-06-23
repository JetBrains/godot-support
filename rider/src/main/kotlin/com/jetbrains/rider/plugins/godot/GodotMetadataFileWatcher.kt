package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent
import com.intellij.openapi.vfs.newvfs.events.VFileCreateEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.nio.file.Path

class GodotMetadataFileWatcher(val project: Project, val mainProjectPath: Path) : AsyncFileListener {

    companion object {
        // Godot 3 paths
        const val oldMonoMetadataRelPath: String = ".mono/metadata/ide_server_meta.txt"

        // Godot 4 paths
        const val monoMetadataRelPath: String = ".godot/mono/metadata/ide_messaging_meta.txt"
        const val metadataRelPath: String = ".godot/editor/project_metadata.cfg"
    }

    @Nullable
    override fun prepareChange(@NotNull events: List<@NotNull VFileEvent>): AsyncFileListener.ChangeApplier? {
        val relevantEvents = events.filter { event ->
            val path = event.path
            (mainProjectPath.resolve(oldMonoMetadataRelPath) == Path.of(path)
             || mainProjectPath.resolve(monoMetadataRelPath) == Path.of(path)
             || mainProjectPath.resolve(metadataRelPath) == Path.of(path)
                ) &&
            (event is VFileContentChangeEvent || event is VFileCreateEvent)
        }

        if (relevantEvents.isEmpty()) return null

        return object : AsyncFileListener.ChangeApplier {
            override fun afterVfsChange() {
                val godotDiscoverer = GodotProjectDiscoverer.getInstance(project)
                if (relevantEvents.any {mainProjectPath.resolve(oldMonoMetadataRelPath) == Path.of(it.path)}) {
                    val newPath = GodotMetadataFileWatcherUtil.getFromMonoMetadataPath(mainProjectPath)
                    newPath?.let { godotDiscoverer.godot3Path.set(it) }
                }

                if (relevantEvents.any {mainProjectPath.resolve(monoMetadataRelPath) == Path.of(it.path)
                    || mainProjectPath.resolve(metadataRelPath) == Path.of(it.path)}) {
                    val newPath = GodotMetadataFileWatcherUtil.getGodot4Path(mainProjectPath)
                    newPath?.let { godotDiscoverer.godot4Path.set(it) }
                }

                // RIDER-127016 Trigger Godot LSP reconnect with Editor start
                godotDiscoverer.projectMetadataModificationSignal.fire(Unit)
            }
        }
    }
}
