package tscn.toolWindow.model

import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread
import gdscript.psi.utils.GdClassUtil
import tscn.psi.TscnFile
import tscn.psi.TscnNodeHeader

data class TreeDependencies(
    val file: PsiFile,
    val treeModel: TscnSceneTreeNode,
    val resourceToNode: HashMap<String, MutableList<SceneNodeDrag>>,
)

object TscnSceneTreeLoader {
    @RequiresBackgroundThread
    fun fromFile(file: PsiFile): SceneTreeBuilder {
        return SceneTreeBuilder(file.project, calculateTreeDependencies(file))
    }

    // Used to move as much from EDT, so we first calculate what
    // we can, so the tree building is just UI work, and not other work.
    private fun calculateTreeDependencies(file: PsiFile): TreeDependencies {
        val treeModel = TscnSceneTreeNode("")
        val nodes = PsiTreeUtil.collectElementsOfType(file, TscnNodeHeader::class.java)
        var parent: String? = null
        nodes.firstOrNull()?.let {
            if (it.instanceResource.isNotBlank()) parent = it.instanceResource
        }
        val typeMemo = hashMapOf<String, String?>()
        val externalTypes = nodes.map {
            // Allow for write actions to take over in order to prevent UI freezes
            ProgressManager.checkCanceled()
            resolveType(file.project, it, memoTable = typeMemo)
        }
        addParentScene(file.project, treeModel, parent)
        val nodeMapping: HashMap<String, MutableList<SceneNodeDrag>> = hashMapOf()
        nodes.forEachIndexed { index, header ->
            ProgressManager.checkCanceled()
            if (header.scriptResource != "") {
                val res = nodeMapping[header.scriptResource]
                if (res == null) {
                    nodeMapping[header.scriptResource] = mutableListOf(
                        SceneNodeDrag.fromHeader(header)
                    )
                } else {
                    res.add(SceneNodeDrag.fromHeader(header))
                }
            }
            treeModel.addNodeChild(header, externalTypes[index])
        }
        return TreeDependencies(file, treeModel, nodeMapping)
    }

    private fun addParentScene(
        project: Project,
        treeModel: TscnSceneTreeNode,
        resource: String?,
        visited: MutableSet<String> = hashSetOf()
    ) {
        if (resource == null || !visited.add(resource)) return
        val typeMemo = HashMap<String, String?>()
        GdClassUtil.getClassIdElement(resource, project)?.let { file ->
            if (file !is TscnFile) return@let

            val nodes = PsiTreeUtil.collectElementsOfType(file, TscnNodeHeader::class.java)
            var parent: String? = null
            nodes.firstOrNull()?.let {
                if (it.instanceResource.isNotBlank()) parent = it.instanceResource
            }
            addParentScene(project, treeModel, parent, visited)
            nodes.forEach {
                treeModel.addNodeChild(
                    it,
                    resolveType(project, it, memoTable = typeMemo),
                    inherited = true
                )
            }
        }
    }

    private fun resolveType(
        project: Project,
        node: TscnNodeHeader,
        visited: MutableSet<String> = hashSetOf(),
        memoTable: MutableMap<String, String?>
    ): String? {
        val instance = node.instanceResource
        if (instance.isBlank()) return null
        if (memoTable.contains(instance)) {
            return memoTable[instance]
        }
        if (!visited.add(instance)) return null

        GdClassUtil.getClassIdElement(instance, project)?.let {
            if (it is TscnFile) PsiTreeUtil.findChildOfType(it, TscnNodeHeader::class.java)?.let { header ->
                val nodeType = header.type
                if (nodeType.isNotEmpty()) {
                    memoTable[instance] = nodeType
                    return nodeType
                }
                val nested = resolveType(project, header, visited, memoTable)
                memoTable[instance] = nested
                return nested
            }
        }
        return null
    }
}
