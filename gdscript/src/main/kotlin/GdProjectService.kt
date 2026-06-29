@file:Suppress("UnstableApiUsage")

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.smartReadAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.diagnostic.ProjectIndexingActivityHistoryListener
import com.intellij.util.indexing.diagnostic.ProjectScanningHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.nio.file.Path

private const val PROJECT_GODOT_FILE_NAME = "project.godot"

@Service(Service.Level.PROJECT)
// not sure how to prevent using it directly from anywhere except GdScriptGodotProjectProvider
// GodotCommunityUtil should be used instead in most cases
class GdProjectService(
    private val project: Project,
    private val scope: CoroutineScope
) {
    private var _projectGodotFile: VirtualFile? = null

    private val _projectBasePathFlow = MutableStateFlow<Path?>(null)
    val projectBasePathFlow: StateFlow<Path?> = _projectBasePathFlow.asStateFlow()

    private val _isPureGdScriptProjectFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    // todo: remove in this plugin. the concept of PureGdScriptProject makes sense only vs csproj/cmakelist
    val isPureGdScriptProjectFlow: StateFlow<Boolean?> = _isPureGdScriptProjectFlow.asStateFlow()

    init {
        // Initial discovery once indexes are ready.
        scheduleDiscovery()

        // todo: I suspect ProjectIndexingActivityHistoryListener is not meant to be used for this purpose
        // After each index scan, check if project.godot appeared (e.g. from a linked folder added later)
        ApplicationManager.getApplication().messageBus.connect(scope).subscribe(
            ProjectIndexingActivityHistoryListener.TOPIC,
            object : ProjectIndexingActivityHistoryListener {
                override fun onFinishedScanning(history: ProjectScanningHistory) {
                    if (history.project != project) return
                    scheduleDiscovery()
                }
            }
        )
    }

    private fun scheduleDiscovery() {
        if (_projectBasePathFlow.value != null) return // already discovered
        scope.launch(Dispatchers.IO) {
            val file = smartReadAction(project) {
                FilenameIndex.firstVirtualFileWithName(
                    PROJECT_GODOT_FILE_NAME, true,
                    GlobalSearchScope.projectScope(project), null
                )
            }
            file?.parent?.let { discoverProject(it) }
        }
    }

    fun discoverProject(dir: VirtualFile) {
        _projectGodotFile = dir.findChild("project.godot")
        _projectBasePathFlow.value = dir.toNioPath()
        thisLogger().trace("discoverProject: dir=$dir")
    }

    companion object {
        fun getInstance(project: Project): GdProjectService = project.service()
    }
}
