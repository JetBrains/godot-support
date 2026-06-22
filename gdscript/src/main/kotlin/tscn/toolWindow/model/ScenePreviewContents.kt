package tscn.toolWindow.model

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListenerBackgroundable
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent
import com.intellij.openapi.vfs.newvfs.events.VFileCreateEvent
import com.intellij.openapi.vfs.newvfs.events.VFileDeleteEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.psi.PsiFile
import com.intellij.ui.components.JBTabbedPane
import com.intellij.util.concurrency.AppExecutorUtil
import com.intellij.util.ui.UIUtil
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import com.jetbrains.rider.godot.community.tscn.TscnFileType
import gdscript.GdScriptBundle
import gdscript.utils.VirtualFileUtil.getPsiFile
import tscn.psi.search.TscnResourceSearcher
import java.awt.BorderLayout
import java.util.concurrent.Callable
import java.util.concurrent.ConcurrentHashMap
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

/**
 * Self-contained scene tree preview.
 * Handles updates, deletions, and creation of nodes,
 * and updates accordingly.
 */
class ScenePreviewContents(val project: Project) : Disposable {

    private val pane: JBTabbedPane = JBTabbedPane()
    val component: JPanel = JPanel(BorderLayout())

    @Volatile
    private var currentTrackedFile: VirtualFile? = null

    private data class Redraw(
        val handledEvents: HashSet<VirtualFile>,
        val treeBuilders: List<SceneTreeBuilder>
    )

    init {
        val vfsListener = object : BulkFileListenerBackgroundable {
            private val storedEvents = ConcurrentHashMap.newKeySet<VirtualFile>()
            override fun after(events: List<VFileEvent>) {
                events.mapNotNullTo(storedEvents) {
                    when (it) {
                        is VFileCreateEvent, is VFileDeleteEvent, is VFileContentChangeEvent -> {
                        }

                        else -> {
                            return@mapNotNullTo null
                        }
                    }
                    it.file?.takeIf { vf ->
                        // Deleted files are not valid,
                        // but we still want to handle them.
                        (vf.isValid || it is VFileDeleteEvent) &&
                            vf.fileType == TscnFileType &&
                            (it is VFileCreateEvent || ProjectRootManager.getInstance(project).fileIndex.isInProject(vf))
                    }
                }
                // Used to check if the calculated changes
                // are or aren't out of date.
                val capturedFile = currentTrackedFile
                ReadAction.nonBlocking(Callable {
                    // We capture and remove on EDT because ReadAction
                    // can get canceled.
                    val handledEvents = HashSet(storedEvents)
                    if (handledEvents.isEmpty()) {
                        return@Callable null
                    }
                    val sceneFiles = collectSceneFiles(capturedFile)
                    Redraw(handledEvents, sceneFiles.map {
                        TscnSceneTreeLoader.fromFile(it)
                    })
                })
                    .inSmartMode(project)
                    .withDocumentsCommitted(project)
                    .expireWith(this@ScenePreviewContents)
                    .coalesceBy(this@ScenePreviewContents, "scene-preview-vfs")
                    .finishOnUiThread(ModalityState.defaultModalityState()) {
                        val noRelevantEvents = it == null
                        if (noRelevantEvents) {
                            return@finishOnUiThread
                        }
                        // At this point events are going to get handled, so it is safe to remove.
                        storedEvents.removeAll(it.handledEvents)
                        val selectionChanged = capturedFile != currentTrackedFile
                        if (selectionChanged) {
                            return@finishOnUiThread
                        }
                        updateFrom(it.treeBuilders)
                    }
                    .submit(AppExecutorUtil.getAppExecutorService())
            }
        }
        project.messageBus.connect(this).subscribe(VirtualFileManager.VFS_CHANGES_BG, vfsListener)
        pane.background = UIUtil.getTreeBackground()
        component.border = BorderFactory.createEmptyBorder()
        component.background = UIUtil.getTreeBackground()
        showNoScene()
    }

    override fun dispose() {}
    fun changeFile(file: VirtualFile?) {
        if (file == currentTrackedFile) {
            return
        }
        ReadAction.nonBlocking(Callable {
            if (file != null) {
                collectSceneFiles(file).map {
                    TscnSceneTreeLoader.fromFile(it)
                }
            } else {
                listOf()
            }
        })
            .inSmartMode(project)
            .expireWith(this@ScenePreviewContents)
            .coalesceBy(this@ScenePreviewContents, "scene-preview-file-change")
            .finishOnUiThread(ModalityState.defaultModalityState()) { deps ->
                currentTrackedFile = file
                if (file != null) {
                    updateFrom(deps)
                } else {
                    showNoScene()
                }
            }.submit(AppExecutorUtil.getAppExecutorService())
    }

    private fun updateFrom(deps: List<SceneTreeBuilder>) {
        val trees = deps.map { preparedTree ->
            preparedTree.build()
        }
        val selectedTab = pane.selectedIndex
        val selectedTitle = if (selectedTab != -1) {
            pane.getTitleAt(pane.selectedIndex)
        } else {
            null
        }
        pane.removeAll()
        if (trees.isEmpty()) {
            showNoScene()
        } else if (trees.size == 1) {
            setContent(trees.first().component)
        } else {
            var newSelections = 0
            deps.zip(trees).sortedBy {
                it.second.name
            }.forEachIndexed { index, pair ->
                val (_, tree) = pair
                if (tree.name == selectedTitle) {
                    newSelections = index
                }
                pane.addTab(tree.name, tree.component)
            }
            pane.selectedIndex = newSelections
            setContent(pane)
        }
    }

    private fun showNoScene() {
        val noSceneLabel = JLabel(GdScriptBundle.message("no.scene"), SwingConstants.CENTER)
        noSceneLabel.verticalAlignment = SwingConstants.CENTER
        setContent(noSceneLabel)
    }

    private fun setContent(newContent: JComponent) {
        component.removeAll()
        component.add(newContent, BorderLayout.CENTER)
        component.revalidate()
        component.repaint()
    }

    private fun collectSceneFiles(file: VirtualFile?): List<PsiFile> {
        if (file == null) {
            return listOf()
        }
        return when (file.fileType) {
            is TscnFileType -> arrayOf(file.getPsiFile(project)).filterNotNull()
            is GdFileType -> TscnResourceSearcher(project).listReference(file).mapNotNull { it.file }.distinct()
            else -> {
                // can't reference CSharpFileType here
                if (file.extension.equals("cs", ignoreCase = true)) {
                    TscnResourceSearcher(project).listReference(file).mapNotNull { it.file }.distinct()
                } else emptyList()
            }
        }
    }
}
