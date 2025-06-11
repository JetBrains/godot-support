import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jetbrains.rd.util.threading.coroutines.launch
import common.util.GdScriptProjectLifetimeService
import gdscript.utils.RiderGodotSupportPluginUtil
import tscn.toolWindow.TscnScenePreviewWindowFactory

class GdScriptToolWindowManagerProjectActivity: ProjectActivity {
    override suspend fun execute(project: Project) {
        val lifetime = GdScriptProjectLifetimeService.getLifetime(project)
        lifetime.launch {
            tryEnableTscnScenePreview(project)
        }
    }

    private suspend fun tryEnableTscnScenePreview(project: Project) {
        val isGodot = RiderGodotSupportPluginUtil.isGodotProject(project)
        if (isGodot == null) {
            // Godot plugin is disabled or not installed
            TscnScenePreviewWindowFactory.makeAvailable(project)
            return
        }
        if (isGodot.await())
            TscnScenePreviewWindowFactory.makeAvailable(project)
    }
}