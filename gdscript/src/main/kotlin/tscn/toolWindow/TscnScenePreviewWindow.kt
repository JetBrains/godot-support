package tscn.toolWindow

import com.intellij.ide.ActivityTracker
import com.intellij.ide.DataManager
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.util.Comparing
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.BitUtil
import com.intellij.util.concurrency.AppExecutorUtil
import com.intellij.util.ui.TimerUtil
import com.intellij.util.ui.UIUtil
import gdscript.GdFileType
import gdscript.psi.utils.GdClassUtil
import gdscript.utils.VirtualFileUtil.getPsiFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tscn.TscnFileType
import tscn.psi.TscnFile
import tscn.psi.TscnNodeHeader
import tscn.psi.utils.TscnResourceUtil
import tscn.toolWindow.model.TscnSceneTreeNode
import java.awt.Container
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
    private val myFileEditor: FileEditor? = null

    private var firstRun = true
    private val pendingRebuild = AtomicBoolean(false)
    private val rebuildRequests =
            MutableSharedFlow<RebuildDelay>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val isStructureViewShowing: Boolean
        get() {
            val windowManager = ToolWindowManager.getInstance(project)
            val toolWindow = windowManager.getToolWindow("Scene preview")
            return toolWindow != null && toolWindow.isVisible
        }

    constructor(project: Project, toolWindow: ToolWindow, coroutineScope: CoroutineScope) {
        this.project = project
        this.toolWindow = toolWindow
        this.coroutineScope = coroutineScope
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
                    rebuildNow("clear a structure on hide")
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
        val insideToolwindow = SwingUtilities.isDescendingFrom(toolWindow.component, owner)
        if (!firstRun && (insideToolwindow || JBPopupFactory.getInstance().isPopupActive)) {
            return
        }
        val dataContext = DataManager.getInstance().getDataContext(owner)
        if (CommonDataKeys.PROJECT.getData(dataContext) !== project) return
        if (insideToolwindow) {
            if (firstRun) {
                setFileFromSelectionHistory()
                firstRun = false
            }
        } else {
            ApplicationManager.getApplication().invokeLater {
                val file = getVirtualFile(dataContext)
                val firstRun = firstRun
                this.firstRun = false

                coroutineScope.launch {
                    if (file != null) {
                        setFile(file)
                    } else if (firstRun) {
                        setFileFromSelectionHistory()
                    } else {
                        // setFile(null)
                    }
                }
            }
        }
    }

    fun rebuildNow(why: String) {
        scheduleRebuild(RebuildDelay.NOW, why)
    }

    private fun getVirtualFile(dataContext: DataContext): VirtualFile? {
        val files = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext)
        return if (files != null && files.size == 1) files[0] else null
    }

    private fun scheduleRebuild() {
        if (!toolWindow.isVisible) return
        scheduleRebuild(RebuildDelay.QUEUE, "delayed rebuild")
    }

    private fun scheduleRebuild(delay: RebuildDelay, why: String) {
        pendingRebuild.set(true)
        check(rebuildRequests.tryEmit(delay))
    }

    private fun clearCaches() {
        rebuildNow("clear caches")
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

    private suspend fun setFile(file: VirtualFile?) {
        if (file == myFile) return
        suspend fun setFileAndRebuild() = withContext(Dispatchers.EDT) {
            // myFile access on EDT
            myFile = file
            scheduleRebuild()
        }

        // File is different
        val differentFiles = withContext(Dispatchers.EDT) { !Comparing.equal(file, myFile) }
        if (differentFiles) {
            setFileAndRebuild()
            return
        }

        // editor is different
        if (file != null) {
            val editorIsDifferent = withContext(Dispatchers.EDT) {
                FileEditorManager.getInstance(project).getSelectedEditor(file) !== myFileEditor
            }
            if (editorIsDifferent) {
                setFileAndRebuild()
                return
            }
        }
    }

    private suspend fun rebuildImpl() = withContext(Dispatchers.EDT) {
        val container: Container = toolWindow.component
        val wasFocused = UIUtil.isFocusAncestor(container)
        val contentManager = toolWindow.contentManager
        contentManager.removeAllContents(true)
        if (!isStructureViewShowing) {
            return@withContext
        }

        val file = myFile ?: run {
            val selectedFiles = FileEditorManager.getInstance(project).selectedFiles
            if (selectedFiles.isNotEmpty()) selectedFiles[0] else null
        }

        if (file != null && file.isValid) {
            myFile = file
        }

        ReadAction.nonBlocking<JComponent> {
            var newPanel: JComponent? = null
            when (myFile?.fileType) {
                is GdFileType -> {
                    myFile?.getPsiFile(project)?.let {
                        val files = TscnResourceUtil.findTscnFilesByResources(it)
                        newPanel = when (files.size) {
                            0 -> createContentPanel(JLabel("No scene"))
                            1 -> buildTreeStructure(files.first())
                            else -> {
                                val tabs = JTabbedPane()
                                files.forEach { tabs.addTab(it.name, buildTreeStructure(it)) }
                                tabs
                            }
                        }
                    }
                }

                is TscnFileType -> {
                    myFile?.getPsiFile(project)?.let {
                        newPanel = buildTreeStructure(it)
                    }
                }
            }
            newPanel
        }
                .coalesceBy(this)
                .finishOnUiThread(ModalityState.defaultModalityState()) {
                    val content = ContentFactory.getInstance()
                            .createContent(it ?: createContentPanel(JLabel("No scene")), null, false)
                    contentManager.addContent(content)
                    pendingRebuild.set(false)
                }
                .submit(AppExecutorUtil.getAppExecutorService())


        if (wasFocused) {
            val policy = container.focusTraversalPolicy
            val component = policy?.getDefaultComponent(container)
            if (component != null) IdeFocusManager.getInstance(project).requestFocusInProject(component, project)
        }
    }

    private fun buildTreeStructure(file: PsiFile): JComponent {
        val nodes = PsiTreeUtil.collectElementsOfType(file, TscnNodeHeader::class.java)
        val treeModel = TscnSceneTreeNode()

        val tree = Tree(treeModel)
        tree.dragEnabled = true
        tree.dropMode
        tree.cellRenderer = TscnSceneCellRenderer()

        var parent: String? = null
        nodes.forEachIndexed { i, it ->
            if (i == 0 && it.instanceResource.isNotBlank()) parent = it.instanceResource
            treeModel.addNodeChild(it, resolveType(it))
        }
        addParentScene(treeModel, tree, parent)

        var j = tree.getRowCount()
        var i = 0
        while (i < j) {
            tree.expandRow(i)
            i++
            j = tree.getRowCount()
        }
        tree.emptyText.text = "No nodes"

        return ScrollPaneFactory.createScrollPane(tree, true)
    }

    private fun addParentScene(treeModel: TscnSceneTreeNode, tree: Tree, resource: String?) {
        if (resource == null) return
        GdClassUtil.getClassIdElement(resource, project)?.let { file ->
            if (file !is TscnFile) return@let

            val nodes = PsiTreeUtil.collectElementsOfType(file, TscnNodeHeader::class.java)
            var parent: String? = null
            nodes.forEachIndexed { i, it ->
                if (i == 0) parent = it.instanceResource
                treeModel.addNodeChild(it, resolveType(it))
            }
            addParentScene(treeModel, tree, parent)
        }
    }

    private fun resolveType(node: TscnNodeHeader): String? {
        val instance = node.instanceResource
        if (instance.isBlank()) return null

        GdClassUtil.getClassIdElement(instance, project)?.let {
            if (it is TscnFile) PsiTreeUtil.findChildOfType(it, TscnNodeHeader::class.java)?.let { return it.type }
            // TODO can there be .gd file?
        }

        return null
    }

    private fun createContentPanel(component: JComponent): JPanel {
        val panel = JPanel()
        panel.background = UIUtil.getTreeBackground()
        panel.add(component)

        return panel
    }

}
