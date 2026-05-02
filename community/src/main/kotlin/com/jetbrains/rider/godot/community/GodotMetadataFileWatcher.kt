package com.jetbrains.rider.godot.community

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import java.nio.file.Path

class GodotMetadataFileWatcher(
    basePath: Path,
    private val onMetadataChanged: (Path) -> Unit
) : AsyncFileListener {

    private val metadataPath: Path = basePath.resolve(METADATA_REL_PATH)

    override fun prepareChange(events: MutableList<out VFileEvent>): AsyncFileListener.ChangeApplier? {
        // Use event.path rather than event.file to catch the create file event
        if (!events.any { event -> Path.of(event.path) == metadataPath }) return null

        return object : AsyncFileListener.ChangeApplier {
            override fun afterVfsChange() {
                thisLogger().trace("project_metadata.cfg afterVfsChange: firing onMetadataChanged for $metadataPath")
                onMetadataChanged(metadataPath)
            }
        }
    }

    companion object {
        const val METADATA_REL_PATH: String = ".godot/editor/project_metadata.cfg"
    }
}
