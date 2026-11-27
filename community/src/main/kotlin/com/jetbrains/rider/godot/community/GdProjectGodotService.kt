package com.jetbrains.rider.godot.community

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.createNestedDisposable
import com.intellij.openapi.util.Version
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.VirtualFile
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
import org.jetbrains.annotations.VisibleForTesting
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString
import kotlin.io.path.readText

@Service(Service.Level.PROJECT)
class GdProjectGodotService(project: Project) {

    /**
     * The info of a supported `project.godot`.
     * [version] is `null` when `config/features` is missing or has no parseable version.
     */
    data class GodotProjectInfo(val version: Version?)

    /** The result of [parseVersion]. */
    sealed interface ParseResult {
        data class Parsed(val version: Version) : ParseResult
        data object Unknown : ParseResult
        data object Unsupported : ParseResult
    }

    private val _projectInfoFlow = MutableStateFlow<GodotProjectInfo?>(null)

    /** `null` when there is no `project.godot`, or when the file is for Godot 3.x. */
    val projectInfoFlow: StateFlow<GodotProjectInfo?> = _projectInfoFlow.asStateFlow()

    /** The engine version from `project.godot`, or `null` when it is not known. */
    val godotVersion: Version?
        get() = projectInfoFlow.value?.version

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
                    is VFileCopyEvent -> event.newParent.localNioPathOrNull()?.resolve(event.newChildName) == projectGodotPath
                    is VFileCreateEvent -> event.parent.localNioPathOrNull()?.resolve(event.childName) == projectGodotPath
                    is VFileMoveEvent -> event.file.localNioPathOrNull() != null &&
                        (Path.of(event.newPath) == projectGodotPath || Path.of(event.oldPath) == projectGodotPath)
                    is VFilePropertyChangeEvent -> event.file.localNioPathOrNull() != null &&
                        (Path.of(event.newPath) == projectGodotPath || Path.of(event.oldPath) == projectGodotPath)
                    else -> event.file?.localNioPathOrNull() == projectGodotPath
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

    // A temporary file can report it being in a local file system without actually having path on disk.
    private fun VirtualFile.localNioPathOrNull(): Path? =
        if (isInLocalFileSystem) fileSystem.getNioPath(this) else null

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

        return when (val result = parseVersion(content)) {
            is ParseResult.Parsed -> GodotProjectInfo(result.version)
            ParseResult.Unknown -> {
                // when opening godot sources as a folder, there are some "project.godot" for tests there
                thisLogger().warn("GdSdk version cannot be parsed from ${projectFile.pathString}")
                GodotProjectInfo(null)
            }
            ParseResult.Unsupported -> {
                thisLogger().warn("Godot 3.x is not supported by the plugin (${projectFile.pathString})")
                null
            }
        }
    }

    companion object {
        private val VERSION_REGEX = "config/features=PackedStringArray\\(.*\"(\\d+\\.\\d+)\".*\\)".toRegex()

        /**
         * Parses the engine version from the [content] of `project.godot`.
         * A follow-up can also read the version from the Godot executable.
         */
        @VisibleForTesting
        fun parseVersion(content: String): ParseResult {
            val versionString = VERSION_REGEX.find(content)?.groups?.get(1)?.value ?: return ParseResult.Unknown
            val version = Version.parseVersion(versionString) ?: return ParseResult.Unknown
            if (version.major == 3) return ParseResult.Unsupported
            return ParseResult.Parsed(version)
        }

        fun getInstance(project: Project): GdProjectGodotService = project.service()
    }
}
