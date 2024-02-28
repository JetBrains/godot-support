package tscn.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import kotlinx.coroutines.CoroutineScope
import tscn.TscnIcon

class TscnScenePreviewWindowFactory(private val coroutineScope: CoroutineScope) : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        toolWindow.setIcon(TscnIcon.TOOL_WINDOW_ICON)
        TscnScenePreviewWindow(project, toolWindow, coroutineScope)
    }

}
