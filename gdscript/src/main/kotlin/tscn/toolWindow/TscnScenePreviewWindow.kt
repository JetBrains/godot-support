package tscn.toolWindow

import com.intellij.openapi.Disposable
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory
import tscn.toolWindow.model.ScenePreviewContents


/**
 * Handles scene preview creation, file changes, focus changes.
 */
class TscnScenePreviewWindow(val project: Project, val toolWindow: ToolWindow) : Disposable {

    private val scenePreview: ScenePreviewContents = ScenePreviewContents(project)

    init {
        val contentManager = toolWindow.contentManager
        val content = ContentFactory.getInstance()
            .createContent(this.scenePreview.component, null, false)
        contentManager.addContent(content)
        Disposer.register(this, scenePreview)
        Disposer.register(toolWindow.contentManager, this)
    }

    fun start() {
        val messageBusHandler = object : FileEditorManagerListener {
            override fun selectionChanged(event: FileEditorManagerEvent) {
                scenePreview.changeFile(event.newFile)
            }
        }
        project.messageBus.connect(this).subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, messageBusHandler)
        ToolWindowManager.getInstance(project).invokeLater {
            val editorManager = FileEditorManager.getInstance(project)
            val alreadyOpenFile = editorManager.selectedEditor?.file
                ?: editorManager.selectedFiles.firstOrNull()
            if (alreadyOpenFile != null) {
                scenePreview.changeFile(alreadyOpenFile)
            }
        }
    }

    override fun dispose() {
    }
}
