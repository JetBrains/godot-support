package tscn.toolWindow

import com.intellij.ide.ActivityTracker
import com.intellij.ide.DataManager
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory
import com.intellij.util.BitUtil
import com.intellij.util.concurrency.AppExecutorUtil
import com.intellij.util.ui.TimerUtil
import com.intellij.util.ui.UIUtil
import common.util.GdScriptProjectLifetimeService
import gdscript.GdScriptBundle
import java.util.concurrent.Callable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import tscn.toolWindow.model.TscnSceneTreeBuilder
import java.awt.KeyboardFocusManager
import java.awt.event.HierarchyEvent
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.*


private enum class RebuildDelay {
    QUEUE,
    NOW,
}

class TscnScenePreviewWindow : Disposable {

    private val project: Project
    private val toolWindow: ToolWindow

    private var activityCount = 0
    private var myFile: VirtualFile? = null
    private val coroutineScope: CoroutineScope

    private val pendingRebuild = AtomicBoolean(false)
    private val rebuildRequests =
        MutableSharedFlow<RebuildDelay>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val isStructureViewShowing: Boolean
        get() {
            val windowManager = ToolWindowManager.getInstance(project)
            val toolWindow = windowManager.getToolWindow(TscnScenePreviewWindowFactory.TOOLWINDOW_ID)
            return toolWindow != null && toolWindow.isVisible
        }

    constructor(project: Project, toolWindow: ToolWindow) {
        this.project = project
        this.toolWindow = toolWindow
        this.coroutineScope = GdScriptProjectLifetimeService.getScope(project)
    }

    fun runScheduler() {
        val component = toolWindow.component

        val timer = TimerUtil.createNamedTimer("SceneView", 100) { _ ->
            if (!component.isShowing) return@createNamedTimer

            val count = ActivityTracker.getInstance().count
            if (count == activityCount) return@createNamedTimer

            val state = ModalityState.stateForComponent(component)
            if (ModalityState.current().dominates(state)) return@createNamedTimer

            val success = runTask { checkUpdate() }
            if (success) activityCount = count // to check on the next turn
        }

        timer.start()
        Disposer.register(this) {
            timer.stop()
        }

        component.addHierarchyListener { e ->
            if (BitUtil.isSet(e.changeFlags, HierarchyEvent.DISPLAYABILITY_CHANGED.toLong())) {
                val visible = toolWindow.isVisible
                if (visible) {
                    runTask { checkUpdate() }
                    scheduleRebuild()
                } else if (!project.isDisposed) {
                    myFile = null
                    rebuildNow()
                }
            }
        }

        if (component.isShowing) {
            runTask { checkUpdate() }
            scheduleRebuild()
        }

        Disposer.register(toolWindow.contentManager, this)
        coroutineScope.launch {
            rebuildRequests.debounce {
                when (it) {
                    RebuildDelay.NOW -> 0L
                    RebuildDelay.QUEUE -> 100
                }
            }
                .collectLatest {
                    rebuildImpl()
                }
        }

        project.messageBus.connect().subscribe<BulkFileListener>(
            VirtualFileManager.VFS_CHANGES,
            object : BulkFileListener {
                override fun after(events: MutableList<out VFileEvent>) {
                    events.find { it.file == myFile }?.let { scheduleRebuild() }
                }
            })
    }

    override fun dispose() {
    }

    private fun runTask(task: Runnable): Boolean {
        try {
            task.run()
            return true
        } catch (exception: Throwable) {
            return false
        }
    }

    private fun checkUpdate() {
        if (project.isDisposed) return
        val owner = KeyboardFocusManager.getCurrentKeyboardFocusManager().focusOwner
        val dataContext = DataManager.getInstance().getDataContext(owner)
        ToolWindowManager.getInstance(project).invokeLater {
            val file = getVirtualFile(dataContext)

            coroutineScope.launch {
                if (file != null) {
                    setFileFromSelectionHistory()
                }
            }
        }
    }

    fun rebuildNow() {
        scheduleRebuild(RebuildDelay.NOW)
    }

    private fun getVirtualFile(dataContext: DataContext): VirtualFile? {
        val files = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext)
        return if (files != null && files.size == 1) files[0] else null
    }

    private fun scheduleRebuild() {
        if (!toolWindow.isVisible) return
        scheduleRebuild(RebuildDelay.QUEUE)
    }

    private fun scheduleRebuild(delay: RebuildDelay) {
        pendingRebuild.set(true)
        check(rebuildRequests.tryEmit(delay))
    }

    private fun setFileFromSelectionHistory() {
        val editorManager = FileEditorManager.getInstance(project) as FileEditorManagerImpl
        val firstInHistory = editorManager.getSelectionHistory().firstOrNull()
        if (firstInHistory != null) {
            coroutineScope.launch {
                setFile(firstInHistory.first)
            }
        }
    }

    private fun setFile(file: VirtualFile?) {
        if (file == myFile) return
        myFile = file
        scheduleRebuild()
    }

    private fun rebuildImpl() {
        ToolWindowManager.getInstance(project).invokeLater {
            val contentManager = toolWindow.contentManager
            contentManager.removeAllContents(true)
            if (!isStructureViewShowing) {
                return@invokeLater
            }

            val file = myFile ?: run {
                val selectedFiles = FileEditorManager.getInstance(project).selectedFiles
                if (selectedFiles.isNotEmpty()) selectedFiles[0] else null
            }

            if (file != null && file.isValid) {
                myFile = file
            }

            ReadAction.nonBlocking(Callable { TscnSceneTreeBuilder(project).build(file) })
                .coalesceBy(this)
                .finishOnUiThread(ModalityState.defaultModalityState()) {
                    val content = ContentFactory.getInstance()
                            .createContent(it ?: createContentPanel(JLabel(GdScriptBundle.message("no.scene"))), null, false)
                    contentManager.addContent(content)
                    pendingRebuild.set(false)
                }
                .submit(AppExecutorUtil.getAppExecutorService())
        }
    }

    private fun createContentPanel(component: JComponent): JPanel {
        val panel = JPanel()
        panel.background = UIUtil.getTreeBackground()
        panel.add(component)

        return panel
    }

}
