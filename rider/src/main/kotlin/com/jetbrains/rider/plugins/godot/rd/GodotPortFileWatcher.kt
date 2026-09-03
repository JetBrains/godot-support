package com.jetbrains.rider.plugins.godot.rd

import com.intellij.openapi.diagnostic.thisLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.ApiStatus
import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds.ENTRY_CREATE
import java.nio.file.StandardWatchEventKinds.ENTRY_DELETE
import java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY
import java.nio.file.StandardWatchEventKinds.OVERFLOW
import kotlin.io.path.isDirectory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * Watches [editorDir] and signals every time the file named [portFileName] inside it might have changed.
 *
 * A [java.nio.file.WatchService] only reports events that happen after the directory has been registered,
 * and the directory itself does not exist until Godot creates it. Therefore an event is never treated as
 * the only source of information: the callback is invoked right after the watch is (re)armed as well, so
 * a port file that appeared while the directory was still missing is not missed.
 */
@ApiStatus.Internal
class GodotPortFileWatcher(
    private val editorDir: Path,
    private val portFileName: String,
    private val pollInterval: Duration = DIRECTORY_POLL_INTERVAL
) {
    suspend fun watch(onChange: suspend () -> Unit) {
        while (true) {
            if (!editorDir.isDirectory()) {
                delay(pollInterval)
                continue
            }
            try {
                watchDirectory(
                    dir = editorDir,
                    // Godot writes the port file once, right after creating the directory, so by the
                    // time the watch is armed the event for it may already be gone - re-read instead.
                    onChange = onChange
                )
            } catch (e: IOException) {
                thisLogger().info("[GODOT RD] failed to watch $editorDir: $e")
                // the port file itself may still be readable, so let the client try anyway
                onChange()
                delay(pollInterval)
                continue
            }
            // the directory may be recreated immediately, the delay keeps this loop from spinning
            delay(WATCH_RESTART_DELAY)
        }
    }

    private suspend fun watchDirectory(
        dir: Path,
        onChange: suspend () -> Unit
    ): Unit = withContext(Dispatchers.IO) {
        FileSystems.getDefault().newWatchService().use { watchService ->
            dir.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE)
            onChange()
            while (true) {
                val key = runInterruptible { watchService.take() }
                val portFileChanged =
                    key.pollEvents().any { event -> event.kind() == OVERFLOW || event.context()?.toString() == portFileName }
                val isValid = key.reset()
                if (portFileChanged || !isValid) {
                    onChange()
                }
                if (!isValid) return@withContext
            }
        }
    }

    companion object {
        private val DIRECTORY_POLL_INTERVAL = 2.seconds
        private val WATCH_RESTART_DELAY = 200.milliseconds
    }
}
