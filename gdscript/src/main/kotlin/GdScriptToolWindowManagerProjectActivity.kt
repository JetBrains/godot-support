import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jetbrains.rd.util.threading.coroutines.launch
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import common.util.GdScriptProjectLifetimeService
import tscn.toolWindow.TscnScenePreviewWindowFactory

class GdScriptToolWindowManagerProjectActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        val lifetime = GdScriptProjectLifetimeService.getLifetime(project)
        lifetime.launch {
            val lifetime = GdScriptProjectLifetimeService.getLifetime(project)
            lifetime.launch {
                GodotCommunityUtil.awaitGodotProject(project)
                TscnScenePreviewWindowFactory.makeAvailable(project)
            }
        }
    }


}

