import com.intellij.openapi.application.readAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.jetbrains.rider.godot.community.GodotMetadataFileWatcher
import com.jetbrains.rider.godot.community.GodotMetadataService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readLines

@Service(Service.Level.PROJECT)
// not sure how to prevent using it directly from anywhere except GdScriptGodotProjectProvider
// GodotCommunityUtil should be used instead in most cases
class GdProjectService(
    private val project: Project,
    private val scope: CoroutineScope
) {
    private var _projectGodotFile: VirtualFile? = null
    private val projectRoot: VirtualFile? get() = _projectGodotFile?.parent

    private val _isGodotProjectDeferred = CompletableDeferred<Boolean>()
    val isGodotProjectDeferred: Deferred<Boolean> get() = _isGodotProjectDeferred

    private val _executablePathFlow = MutableStateFlow<Path?>(null)
    val executablePathFlow: StateFlow<Path?> = _executablePathFlow.asStateFlow()

    private val _projectBasePathFlow = MutableStateFlow<Path?>(null)
    val projectBasePathFlow: StateFlow<Path?> = _projectBasePathFlow.asStateFlow()

    private val _isPureGdScriptProjectFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isPureGdScriptProjectFlow: StateFlow<Boolean?> = _isPureGdScriptProjectFlow.asStateFlow()

    fun getExecutablePath(): Path? = projectRoot?.let { getGodot4Path(it.toNioPath()) }

    init {
        val projectDir = project.guessProjectDir()
        val projectGodot = projectDir?.findChild("project.godot")
        if (projectDir != null && projectGodot != null) {
            discoverProject(projectDir)
        } else {
            // todo: react on further changes of the VFS or the index, I am not sure
            DumbService.getInstance(project).runWhenSmart { // Wait for indexes to finish
                scope.launch(Dispatchers.IO){
                    val projectGodotFile = readAction {
                        FilenameIndex.firstVirtualFileWithName("project.godot", true,
                            GlobalSearchScope.projectScope(project), null
                        )
                    }
                    projectGodotFile?.parent?.let { discoverProject(it) }
                }
            }
        }
    }

    fun discoverProject(projectDir: VirtualFile) {
        _projectGodotFile = projectDir.findChild("project.godot")
        _projectBasePathFlow.value = projectDir.toNioPath()
        _isPureGdScriptProjectFlow.value = _projectBasePathFlow.value != null
        _executablePathFlow.value = getExecutablePath()
        _isGodotProjectDeferred.complete(_projectGodotFile != null)

        subscribeToMetadataChanges()
    }

    private fun subscribeToMetadataChanges() {
        val metadataService = GodotMetadataService.getInstance(project)
        scope.launch {
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
