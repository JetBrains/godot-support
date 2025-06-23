package tscn.toolWindow

import GdScriptBundle
import com.intellij.openapi.application.EDT
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import common.util.GdScriptProjectLifetimeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TscnScenePreviewWindowFactory() : ToolWindowFactory {
    companion object {
        const val TOOLWINDOW_ID = "TscnScenePreviewWindowId"

        fun makeAvailable(project: Project) {
            GdScriptProjectLifetimeService.getScope(project).launch(Dispatchers.EDT) {
                val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(TOOLWINDOW_ID) ?: return@launch
                toolWindow.isAvailable = true
            }
        }
    }

    override fun init(toolWindow: ToolWindow) {
        toolWindow.stripeTitle = GdScriptBundle.message("tab.title.scene.preview")
        super.init(toolWindow)
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val window = TscnScenePreviewWindow(project, toolWindow)
        window.runScheduler()
    }

    override fun shouldBeAvailable(project: Project): Boolean {
        return false
    }

    override suspend fun isApplicableAsync(project: Project): Boolean {
        // I have tried to await() IsGodotProject here, but it causes all toolwindows to wait, so can't use it
        return super.isApplicableAsync(project)
    }
}
