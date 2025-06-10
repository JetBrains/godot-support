package tscn.toolWindow

import GdScriptPluginIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import common.util.GdScriptProjectLifetimeService
import gdscript.utils.RiderGodotSupportPluginUtil
import kotlinx.coroutines.CompletableDeferred

class TscnScenePreviewWindowFactory() : ToolWindowFactory {

    override fun init(toolWindow: ToolWindow) {
        super.init(toolWindow)
        toolWindow.setIcon(GdScriptPluginIcons.TscnIcons.TOOL_WINDOW_ICON)
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val window = TscnScenePreviewWindow(project, toolWindow)
        window.runScheduler()
    }

    override suspend fun isApplicableAsync(project: Project): Boolean {
        val property = RiderGodotSupportPluginUtil.getMainProjectBasePathProperty(project) ?: return true // no Godot plugin

        val deferred = CompletableDeferred<Unit>()
        property.advise(GdScriptProjectLifetimeService.getLifetime(project)) { value ->
            deferred.complete(Unit)
        }
        deferred.await()
        return property.value != null
    }
}
