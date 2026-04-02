package gdscript.library

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.createNestedDisposable
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.VirtualFileManager
import com.jetbrains.rd.util.lifetime.SequentialLifetimes
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import common.util.GdScriptProjectLifetimeService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.nio.charset.Charset
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString
import kotlin.io.path.readText

@Service(Service.Level.PROJECT)
class GdProjectGodotService(@Suppress("unused") project: Project) {

    data class GodotProjectInfo(val version: String)

    private val _projectInfoFlow = MutableStateFlow<GodotProjectInfo?>(null)
    val projectInfoFlow: StateFlow<GodotProjectInfo?> = _projectInfoFlow.asStateFlow()

    private val sequentialLifetimes = SequentialLifetimes(GdScriptProjectLifetimeService.getLifetime(project))

    init {
        GdScriptProjectLifetimeService.getScope(project).launch {
            GodotCommunityUtil.getGodotProjectBasePathFlow(project).collect { basePath ->
                if (basePath == null) return@collect
                watch(basePath)
            }
        }
    }

    fun watch(basePath: Path) {
        val lifetime = sequentialLifetimes.next()
        _projectInfoFlow.value = parseProjectGodot(basePath)

        val projectGodotPath = basePath.resolve("project.godot")
        val listener = AsyncFileListener { events ->
            val hasChange = events.any { event ->
                val file = event.file ?: return@any false
                file.isInLocalFileSystem && file.toNioPath() == projectGodotPath
            }
            if (!hasChange) null
            else object : AsyncFileListener.ChangeApplier {
                override fun afterVfsChange() {
                    _projectInfoFlow.value = parseProjectGodot(basePath)
                }
            }
        }
        VirtualFileManager.getInstance().addAsyncFileListener(listener, lifetime.lifetime.createNestedDisposable())
    }

    private fun parseProjectGodot(basePath: Path): GodotProjectInfo? {
        val projectFile = basePath.resolve("project.godot")
        if (!projectFile.exists()) return null
        val content = projectFile.readText(Charset.defaultCharset())

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
