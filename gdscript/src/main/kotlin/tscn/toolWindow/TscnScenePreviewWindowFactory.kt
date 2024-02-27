package tscn.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.psi.search.FilenameIndex
import gdscript.utils.ProjectUtil.contentScope
import kotlinx.coroutines.CoroutineScope

class TscnScenePreviewWindowFactory(private val coroutineScope: CoroutineScope) : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        TscnScenePreviewWindow(project, toolWindow, coroutineScope)
//        val panel = TscnScenePreviewWindow(project, toolWindow, coroutineScope).myPanel

//        val content = ContentFactory.getInstance().createContent(panel, null, false)
//        toolWindow.component.putClientProperty(ToolWindowContentUi.HIDE_ID_LABEL, true)
//        toolWindow.contentManager.addContent(content)
    }

    override suspend fun isApplicableAsync(project: Project): Boolean {
        return true
        return FilenameIndex.getVirtualFilesByName("project.godot", project.contentScope()).isNotEmpty()
    }

}
