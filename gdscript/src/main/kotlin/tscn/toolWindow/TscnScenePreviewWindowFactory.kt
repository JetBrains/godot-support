package tscn.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class TscnScenePreviewWindowFactory() : ToolWindowFactory {

    override fun init(toolWindow: ToolWindow) {
        super.init(toolWindow)
        toolWindow.setIcon(GdScriptPluginIcons.TscnIcons.TOOL_WINDOW_ICON)
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val window = TscnScenePreviewWindow(project, toolWindow)
        window.runScheduler()
    }

}
