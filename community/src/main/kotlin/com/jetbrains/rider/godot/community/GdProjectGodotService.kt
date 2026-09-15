package com.jetbrains.rider.godot.community

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.createNestedDisposable
import com.intellij.openapi.util.Version
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.events.VFileCopyEvent
import com.intellij.openapi.vfs.newvfs.events.VFileCreateEvent
import com.intellij.openapi.vfs.newvfs.events.VFileMoveEvent
import com.intellij.openapi.vfs.newvfs.events.VFilePropertyChangeEvent
import com.jetbrains.rd.util.lifetime.SequentialLifetimes
import com.jetbrains.rd.util.lifetime.isAlive
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString
import kotlin.io.path.readText

@Service(Service.Level.PROJECT)
class GdProjectGodotService(project: Project) {

    data class GodotProjectInfo(val version: String) {
        /** Parsed version; `null` for unparseable values like "Master" */
        val parsedVersion: Version? by lazy { Version.parseVersion(version) }
    }

    private val _projectInfoFlow = MutableStateFlow<GodotProjectInfo?>(null)
    val projectInfoFlow: StateFlow<GodotProjectInfo?> = _projectInfoFlow.asStateFlow()

    private val _currentSceneFlow = MutableStateFlow<String?>(null)

    /**
     * Currently for future consumers.
     * "" -> no scene opened,
     *
     * null -> godot is not connected
     */
    val currentSceneFlow: StateFlow<String?> = _currentSceneFlow.asStateFlow()

    private val sequentialLifetimes = SequentialLifetimes(GdScriptProjectLifetimeService.getLifetime(project))
    // A late event from the previous Godot project folder must not overwrite the info of the current folder.
    // Each watch() call increments the generation. The lock keeps the generation check and the write in one step.
    private val watchLock = Any()
    private var watchGeneration = 0L

    init {
        GdScriptProjectLifetimeService.getScope(project).launch {
            GodotCommunityUtil.getGodotProjectBasePathFlow(project).collect { basePath ->
                if (basePath == null) return@collect
                watch(basePath)
            }
        }
    }

    fun updateCurrentScene(newScene: String?) {
        _currentSceneFlow.value = newScene
    }

    private fun watch(basePath: Path) {
        val info = parseProjectGodot(basePath)
        val (generation, lifetime) = synchronized(watchLock) {
            val generation = ++watchGeneration
            val lifetime = sequentialLifetimes.next()
            _projectInfoFlow.value = info
            generation to lifetime
        }

        val projectGodotPath = basePath.resolve("project.godot")
        val listener = AsyncFileListener { events ->
            // A version change in project.godot starts a docs load, so the watcher must see every way the file changes.
            // A program can replace the file by a create, a copy, a move or a rename. The Godot editor can save this way.
            // A move or a rename also counts when project.godot is the old path, because the file then disappears.
            val hasChange = events.any { event ->
                when (event) {
                    is VFileCopyEvent -> event.newParent.isInLocalFileSystem &&
                        event.newParent.toNioPath().resolve(event.newChildName) == projectGodotPath
                    is VFileCreateEvent -> event.parent.isInLocalFileSystem &&
                        event.parent.toNioPath().resolve(event.childName) == projectGodotPath
                    is VFileMoveEvent -> event.file.isInLocalFileSystem &&
                        (Path.of(event.newPath) == projectGodotPath || Path.of(event.oldPath) == projectGodotPath)
                    is VFilePropertyChangeEvent -> event.file.isInLocalFileSystem &&
                        (Path.of(event.newPath) == projectGodotPath || Path.of(event.oldPath) == projectGodotPath)
                    else -> event.file?.let { it.isInLocalFileSystem && it.toNioPath() == projectGodotPath } == true
                }
            }
            if (!hasChange) null
            else object : AsyncFileListener.ChangeApplier {
                override fun afterVfsChange() {
                    val info = parseProjectGodot(basePath)
                    synchronized(watchLock) {
                        if (generation == watchGeneration && lifetime.isAlive) _projectInfoFlow.value = info
                    }
                }
            }
        }
        VirtualFileManager.getInstance().addAsyncFileListener(listener, lifetime.lifetime.createNestedDisposable())
    }

    private fun parseProjectGodot(basePath: Path): GodotProjectInfo? {
        val projectFile = basePath.resolve("project.godot")
        if (!projectFile.exists()) return null
        val content = try {
            projectFile.readText()
        }
        catch (e: IOException) {
            // An exception here would stop the base path collector before watch() registers the listener.
            // Then a later fix of the file would start no docs load.
            thisLogger().warn("Failed to read ${projectFile.pathString}", e)
            return null
        }

        // todo: use com.intellij.openapi.util.Version instead of string
        // todo: get full version from the FileVersionInfo on Windows, Contents/Info.plist on Mac, parse file name on Linux
        // also possible to run `godot --version` and parse the output
        var version = VERSION_REGEX.find(content)?.groups?.get(1)?.value
        if (version == null) {
            version = "Master"
            // when opening godot sources as a folder, there are some "project.godot" for tests there
            thisLogger().warn("GdSdk version cannot be parsed from ${projectFile.pathString}")
        }
        if (version.startsWith("3.")) {
            thisLogger().warn("Godot 3.x is not supported by the plugin (${projectFile.pathString})")
            return null
        }

        return GodotProjectInfo(version)
    }

    companion object {
        private val VERSION_REGEX = "config/features=PackedStringArray\\(.*\"(\\d\\.\\d)\".*\\)".toRegex()

        fun getInstance(project: Project): GdProjectGodotService = project.service()
    }
}
