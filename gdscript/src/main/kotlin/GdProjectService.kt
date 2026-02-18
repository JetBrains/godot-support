import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.rider.godot.community.GodotMetadataFileWatcher
import com.jetbrains.rider.godot.community.GodotMetadataService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readLines

@Service(Service.Level.PROJECT)
class GdProjectService(
    private val project: Project,
    private val cs: CoroutineScope
) {
    private var _projectGodotFile: VirtualFile? = null
    val projectGodotFile: VirtualFile? get() = _projectGodotFile
    val projectRoot: VirtualFile? get() = _projectGodotFile?.parent

    val isGodotProject: Boolean get() = _projectGodotFile != null

    private val _isGodotProjectDeferred = CompletableDeferred<Boolean>()
    val isGodotProjectDeferred: Deferred<Boolean> get() = _isGodotProjectDeferred

    private fun getGodotProjectBasePath(project: Project): VirtualFile? = project.basePath?.let { VfsUtil.findFile(Path.of(it), false) }

    private val _executablePathFlow = MutableStateFlow<Path?>(null)
    val executablePathFlow: StateFlow<Path?> = _executablePathFlow.asStateFlow()

    private val _projectBasePathFlow = MutableStateFlow<Path?>(null)
    val projectBasePathFlow: StateFlow<Path?> = _projectBasePathFlow.asStateFlow()

    private val _isPureGdScriptProjectFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isPureGdScriptProjectFlow: StateFlow<Boolean?> = _isPureGdScriptProjectFlow.asStateFlow()

    fun getExecutablePath(): Path? = getGodotProjectBasePath(project)?.let { getGodot4Path(it.toNioPath()) }

    fun discoverProject() {
        val basePath = getGodotProjectBasePath(project)
        _projectGodotFile = basePath?.findChild("project.godot")
        _projectBasePathFlow.value = projectRoot?.toNioPath()
        _isPureGdScriptProjectFlow.value = _projectBasePathFlow.value != null
        _executablePathFlow.value = getExecutablePath()
        _isGodotProjectDeferred.complete(isGodotProject)

        subscribeToMetadataChanges()
    }

    private fun subscribeToMetadataChanges() {
        val metadataService = GodotMetadataService.getInstance(project)
        cs.launch {
            metadataService.metadataChangeFlow.collect { event ->
                if (event != null) {
                    updateExecutablePathFromMetadata(event.metadataPath)
                }
            }
        }
    }

    private fun updateExecutablePathFromMetadata(metadataPath: Path) {
        val executablePath = readExecutablePathFromMetadata(metadataPath)
        _executablePathFlow.value = executablePath
    }

    companion object {
        fun getInstance(project: Project): GdProjectService = project.service()

        private fun getGodot4Path(projectPath: Path): Path? {
            val projectMetadataCfg = projectPath.resolve(GodotMetadataFileWatcher.METADATA_REL_PATH)
            return readExecutablePathFromMetadata(projectMetadataCfg)
        }

        private fun readExecutablePathFromMetadata(metadataPath: Path): Path? {
            if (!metadataPath.exists()) return null
            return metadataPath.readLines()
                .firstOrNull { it.startsWith("executable_path") }
                ?.substringAfter("executable_path=")
                ?.trim('"')
                ?.let { Path.of(it) }
                ?.takeIf { it.exists() }
        }
    }
}
