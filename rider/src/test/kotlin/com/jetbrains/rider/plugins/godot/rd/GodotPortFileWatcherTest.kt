package com.jetbrains.rider.plugins.godot.rd

import com.jetbrains.rider.test.shared.constants.TeamCityTags
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Tag(TeamCityTags.Plugins.Godot.General)
@Timeout(120)
class GodotPortFileWatcherTest {
    @TempDir
    lateinit var projectDir: Path

    @Test
    fun `the port file present before the watch is armed is reported`() = runBlocking {
        val editorDir = projectDir.resolve(EDITOR_DIR)
        editorDir.createDirectories()
        writePortFile(editorDir)
        val changes = Channel<Unit>(Channel.CONFLATED)
        val watcher = launchWatcher(editorDir, changes)
        try {
            // nothing changes anymore, so no filesystem event will ever be delivered
            withTimeout(ARM_TIMEOUT) { changes.receive() }
        } finally {
            watcher.cancel()
        }
    }

    @Test
    fun `the port file appearing while the directory is missing is reported`() = runBlocking {
        val editorDir = projectDir.resolve(EDITOR_DIR)
        val changes = Channel<Unit>(Channel.CONFLATED)
        val watcher = launchWatcher(editorDir, changes)
        try {
            // let the watcher notice the missing directory and start polling for it
            delay(POLL_INTERVAL * 4)
            // the directory shows up with the port file already in it, like it does when Godot
            // starts: every event for the port file happens before the directory can be watched
            val staged = projectDir.resolve("staged-editor")
            staged.createDirectories()
            writePortFile(staged)
            editorDir.parent.createDirectories()
            Files.move(staged, editorDir, StandardCopyOption.ATOMIC_MOVE)

            withTimeout(ARM_TIMEOUT) { changes.receive() }
        } finally {
            watcher.cancel()
        }
    }

    @Test
    fun `the port file written into a watched directory is reported`() = runBlocking {
        val editorDir = projectDir.resolve(EDITOR_DIR)
        editorDir.createDirectories()
        val changes = Channel<Unit>(Channel.CONFLATED)
        val watcher = launchWatcher(editorDir, changes)
        try {
            // the state of the empty directory, reported once the watch is armed
            withTimeout(ARM_TIMEOUT) { changes.receive() }

            writePortFile(editorDir)

            // the timeout is generous on purpose: on some platforms the JDK falls back
            // to a polling watch service with a sensitivity of several seconds
            withTimeout(EVENT_TIMEOUT) { changes.receive() }
        } finally {
            watcher.cancel()
        }
    }

    private fun CoroutineScope.launchWatcher(editorDir: Path, changes: SendChannel<Unit>) = launch(Dispatchers.IO) {
        GodotPortFileWatcher(editorDir, PORT_FILE_NAME, pollInterval = POLL_INTERVAL).watch {
            changes.send(Unit)
        }
    }

    private fun writePortFile(dir: Path) {
        dir.resolve(PORT_FILE_NAME).writeText("port=63342\nmodelHash=1\n")
    }

    companion object {
        private const val EDITOR_DIR = ".godot/editor"
        private const val PORT_FILE_NAME = "rider_ide_server.cfg"
        private val POLL_INTERVAL = 50.milliseconds
        private val ARM_TIMEOUT = 10.seconds
        private val EVENT_TIMEOUT = 60.seconds
    }
}
