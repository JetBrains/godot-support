import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jetbrains.rd.util.threading.coroutines.launch
import com.jetbrains.rider.godot.community.GdScriptProjectLifetimeService
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import tscn.toolWindow.TscnScenePreviewWindowFactory
import tscn.toolWindow.model.ModifierTracker
import tscn.toolWindow.model.SceneTreeEditorDropHandler

class GdScriptToolWindowManagerProjectActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        GdScriptProjectLifetimeService.getLifetime(project).launch {
            GodotCommunityUtil.awaitGodotProject(project)
            if(!ApplicationManager.getApplication().isHeadlessEnvironment) {
                ModifierTracker.getInstance()
            }
            TscnScenePreviewWindowFactory.makeAvailable(project)
            SceneTreeEditorDropHandler.installIntoExistingEditors(project)
        }
    }
}
