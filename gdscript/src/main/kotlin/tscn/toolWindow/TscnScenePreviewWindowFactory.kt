package tscn.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.search.FileTypeIndex
import gdscript.utils.ProjectUtil.contentScope
import project.ProjectFileType
import tscn.TscnIcon

class TscnScenePreviewWindowFactory() : ToolWindowFactory {

    override fun init(toolWindow: ToolWindow) {
        super.init(toolWindow)
        toolWindow.setIcon(TscnIcon.TOOL_WINDOW_ICON)
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val window = TscnScenePreviewWindow(project, toolWindow)
        window.runScheduler()
    }

}
