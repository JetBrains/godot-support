package com.jetbrains.rider.godot.community

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import java.nio.file.Path

internal class GodotMetadataFileWatcher(
    private val absolutePath: Path,
    private val onChange: () -> Unit
) : AsyncFileListener {

    override fun prepareChange(events: MutableList<out VFileEvent>): AsyncFileListener.ChangeApplier? {
        // VFS may coalesce creation/deletion events to an ancestor directory
        // this matches both folder creation and a specific file change - no over-firing on other files changes
        if (!events.any { event -> absolutePath.startsWith(Path.of(event.path)) }) return null

        return object : AsyncFileListener.ChangeApplier {
            override fun afterVfsChange() {
                thisLogger().trace("$absolutePath afterVfsChange: firing onChange")
                onChange()
            }
        }
    }
}
