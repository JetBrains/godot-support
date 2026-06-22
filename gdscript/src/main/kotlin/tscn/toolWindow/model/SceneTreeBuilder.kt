package tscn.toolWindow.model

import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsSafe
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.concurrency.AppExecutorUtil
import com.intellij.util.concurrency.annotations.RequiresEdt
import com.intellij.util.ui.tree.TreeUtil
import gdscript.GdScriptBundle
import gdscript.index.impl.GdFileResIndex
import tscn.toolWindow.TscnSceneCellRenderer
import java.awt.datatransfer.Transferable
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.concurrent.Callable
import javax.swing.JComponent
import javax.swing.TransferHandler
import javax.swing.tree.TreeSelectionModel

data class LabelledTree(@param:NlsSafe val name: String, val component: JComponent)

class SceneTreeBuilder(private val project: Project, private val deps: TreeDependencies) {
    @RequiresEdt
    fun build(): LabelledTree {
        return LabelledTree(deps.file.name, buildTreeStructure(deps))
    }

    private fun buildTreeStructure(deps: TreeDependencies): JComponent {
        val tree = Tree(deps.treeModel)

        tree.dragEnabled = true
        tree.selectionModel.selectionMode = TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION
        tree.transferHandler = object : TransferHandler() {
            override fun getSourceActions(c: JComponent): Int = COPY
            override fun createTransferable(c: JComponent): Transferable? {
                val tree = (c as? Tree) ?: return null
                val nodes = tree.selectionPaths.orEmpty().mapNotNull {
                    it.lastPathComponent as? TscnSceneTreeNode
                }.map {
                    SceneNodeDrag(
                        nodeType = it.myType,
                        nodeParentPath = it.parentPath,
                        nodeName = it.myName,
                        isUnique = it.hasUniqueName
                    )
                }
                if (nodes.isEmpty()) return null

                return SceneNodeTransferable(SceneDragPayload(deps.resourceToNode, nodes))
            }
        }
        tree.cellRenderer = TscnSceneCellRenderer(project)
        tree.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val node = tree.getClosestPathForLocation(e.x, e.y)?.lastPathComponent as? TscnSceneTreeNode? ?: return

                val index = (tree.width - e.x) / TscnSceneCellRenderer.BUTTON_WIDTH
                val action = node.listActions().reversed().getOrNull(index) ?: return
                callAction(node, action)
            }
        })

        TreeUtil.expandAll(tree)
        tree.emptyText.text = GdScriptBundle.message("gdscript.scene.tree.no.nodes")

        return ScrollPaneFactory.createScrollPane(tree, true)
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

            "unique", "visible" -> {}
        }
    }
}
