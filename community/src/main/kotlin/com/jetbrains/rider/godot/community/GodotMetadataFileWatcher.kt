package com.jetbrains.rider.godot.community

import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.isFile
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import java.nio.file.Path
import kotlin.io.path.pathString

class GodotMetadataFileWatcher(
    basePath: Path,
    private val onMetadataChanged: (Path) -> Unit
) : AsyncFileListener {

    private val metadataPath: Path = basePath.resolve(METADATA_REL_PATH)

    override fun prepareChange(events: MutableList<out VFileEvent>): AsyncFileListener.ChangeApplier? {
        val hasMetadataChange = events.any { event ->
            val file = event.file ?: return@any false
            file.isInLocalFileSystem && file.toNioPath() == metadataPath
        }

        if (!hasMetadataChange) return null

        return object : AsyncFileListener.ChangeApplier {
            override fun afterVfsChange() {
                onMetadataChanged(metadataPath)
            }
        }
    }

    companion object {
        const val METADATA_REL_PATH: String = ".godot/editor/project_metadata.cfg"
    }
}
