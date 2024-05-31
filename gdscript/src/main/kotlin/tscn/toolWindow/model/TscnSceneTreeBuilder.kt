package tscn.toolWindow.model

import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.concurrency.AppExecutorUtil
import com.jetbrains.rd.util.Callable
import gdscript.GdFileType
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.utils.GdClassUtil
import gdscript.utils.VirtualFileUtil.getPsiFile
import tscn.TscnFileType
import tscn.psi.TscnFile
import tscn.psi.TscnNodeHeader
import tscn.psi.search.TscnResourceSearcher
import tscn.toolWindow.TscnSceneCellRenderer
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JComponent

class TscnSceneTreeBuilder {

    val project: Project

    constructor(project: Project) {
        this.project = project
    }

    fun build(file: VirtualFile?): JComponent? {
        file ?: return null

        val trees = collectSceneFiles(file, project)
            .let { it.ifEmpty { return null } }
            .map { buildTree(it) }

        return if (trees.size == 1) {
            trees.first().second
        } else {
            val tabs = JBTabbedPane()
            trees.forEach { tabs.addTab(it.first, it.second) }
            tabs
        }
    }

    private fun collectSceneFiles(file: VirtualFile, project: Project): List<PsiFile> {
        return when (file.fileType) {
            is TscnFileType -> arrayOf(file.getPsiFile(project)).filterNotNull()
            is GdFileType -> TscnResourceSearcher(project).listReference(file).mapNotNull {
                it.file
            }

            else -> emptyList()
        }
    }

    private fun buildTree(file: PsiFile): Pair<String, JComponent> {
        return Pair(file.name, buildTreeStructure(file, ""))
    }

    private fun buildTreeStructure(file: PsiFile, basePath: String): JComponent {
        val nodes = PsiTreeUtil.collectElementsOfType(file, TscnNodeHeader::class.java)
        val treeModel = TscnSceneTreeNode(basePath)

        val tree = Tree(treeModel)
        tree.dragEnabled = false
        tree.dropMode
        tree.cellRenderer = TscnSceneCellRenderer(project)
        tree.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val node = tree.getClosestPathForLocation(e.x, e.y).lastPathComponent as TscnSceneTreeNode? ?: return

                val index = (tree.width - e.x) / TscnSceneCellRenderer.BUTTON_WIDTH
                val action = node.listActions().reversed().getOrNull(index) ?: return
                callAction(node, action)
            }
        })

        var parent: String? = null
        nodes.first()?.let {
            if (it.instanceResource.isNotBlank()) parent = it.instanceResource
        }
        addParentScene(treeModel, tree, parent)
        nodes.forEach { treeModel.addNodeChild(it, resolveType(it)) }

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
            nodes.first()?.let {
                if (it.instanceResource.isNotBlank()) parent = it.instanceResource
            }
            addParentScene(treeModel, tree, parent)
            nodes.forEach { treeModel.addNodeChild(it, resolveType(it), inherited = true) }
        }
    }

    private fun resolveType(node: TscnNodeHeader): String? {
        val instance = node.instanceResource
        if (instance.isBlank()) return null

        GdClassUtil.getClassIdElement(instance, project)?.let {
            if (it is TscnFile) PsiTreeUtil.findChildOfType(it, TscnNodeHeader::class.java)?.let {
                return it.type.ifEmpty { resolveType(it) } ?: ""
            }
        }

        return null
    }

    private fun callAction(node: TscnSceneTreeNode, action: String) {
        when (action) {
            "instance", "script" -> {
                ReadAction.nonBlocking(Callable {
                    GdFileResIndex.getFiles(node.resource, project).firstOrNull()
                })
                    .finishOnUiThread(ModalityState.defaultModalityState()) {
                        it?.let { OpenFileDescriptor(project, it).navigate(true) }
                    }
                    .submit(AppExecutorUtil.getAppExecutorService())
            }

            "unique" -> {}
            "visible" -> {}
        }
    }

}
