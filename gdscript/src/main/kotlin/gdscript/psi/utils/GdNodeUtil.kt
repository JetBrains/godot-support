package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.impl.GdFileResIndex
import gdscript.model.GdNodeHolder
import gdscript.psi.GdNodePath
import gdscript.utils.PsiElementUtil.psi
import gdscript.utils.VirtualFileUtil.getPsiFile
import gdscript.utils.VirtualFileUtil.resourcePath
import tscn.index.impl.TscnNodeIndex
import tscn.index.impl.TscnResourceIndex
import tscn.psi.TscnNodeHeader
import tscn.psi.TscnParagraph
import tscn.psi.TscnResourceHeader
import tscn.psi.utils.TscnNodeUtil
import tscn.psi.utils.TscnResourceUtil
import kotlin.io.path.Path
import kotlin.io.path.relativeTo

/**
 * Node utils for available nodes from given script
 */
object GdNodeUtil {

    /**
     * Returns corresponding node for given NodePath element
     */
    fun findNode(element: GdNodePath): GdNodeHolder? {
        val nodes = listNodes(element)
        val path = element.text
            .trim('$', '%', '"', '^')
            .split(":")
            .first()

        return nodes.find { it.relativePath.trim('$') == path || it.uniqueId == path };
    }

    /**
     * List all available nodes for given file with parsed relative paths
     */
    fun listNodes(element: PsiElement): Array<GdNodeHolder> {
        val scripts = TscnResourceUtil.findTscnByResources(element)
        if (scripts.isEmpty()) return emptyArray()

        val connectedNodes = scripts.flatMap { listConnectedNodesForResource(it) }

        return connectedNodes.flatMap { listAvailableNodeForNode(it, connectedNodes.size <= 1) }
            .toTypedArray()
    }

    private fun listConnectedNodesForResource(resource: TscnResourceHeader): Iterable<TscnNodeHeader> {
        val path = resource.path

        return PsiTreeUtil.findChildrenOfType(resource.containingFile, TscnNodeHeader::class.java)
            .filter { it.scriptResource == path }
    }

    private fun listAvailableNodeForNode(resourceNode: TscnNodeHeader, isSingleNode: Boolean): Iterable<GdNodeHolder> {
        val resultSet = mutableListOf<GdNodeHolder>()
        availableNodes(
            resourceNode.containingFile,
            resourceNode.nodePath,
            resultSet,
            isSingleNode,
        )

        return resultSet
    }

    private fun availableNodes(
        tscnFile: PsiFile,
        basePath: String,
        resultSet: MutableList<GdNodeHolder>,
        isSingleNode: Boolean,
        parentPath: String = "",
    ) {
        val nodes = PsiTreeUtil.findChildrenOfType(tscnFile, TscnNodeHeader::class.java)
        val baseName = if (isSingleNode) "" else basePath.split("/").last()

        nodes.forEach {
            // In case of nested instance simply joining paths lead to "Parent/../Child"
            // so skip pureParent which would be duplicated and remove prefix of it
            var currentNodePath = it.nodePath
            if (parentPath.isNotBlank()) {
                currentNodePath = currentNodePath.removePrefix("..")
                if (currentNodePath.isBlank()) return@forEach
            }

            val nodePath = "$parentPath$currentNodePath"

            val instancePath = it.instanceResource
            if (instancePath.isNotBlank()) {
                val instance = GdFileResIndex.getFiles(instancePath, tscnFile.project).firstOrNull()
                if (instance != null) {
                    availableNodes(
                        instance.getPsiFile(tscnFile.project)!!,
                        basePath,
                        resultSet,
                        isSingleNode,
                        nodePath,
                    )
                }
            }

            var relativePath = Path(nodePath).relativeTo(Path(basePath)).toString().replace("\\", "/")
            if (relativePath.isBlank()) {
                relativePath = "."
            }
            if (relativePath.contains(".")) {
                relativePath = "\"$relativePath\""
            }

            var hint = nodePath.removePrefix("../")
            var tail: String? = null
            if (hint == "..") {
                hint = it.name
                tail = "(root)"
            }

            if (!isSingleNode) {
                hint = "$baseName->$hint"
            }

            var uniqueId: String? = null
            if (it.isUniqueNameOwner) {
                uniqueId = "%${it.name}"
                tail = " ${it.name}"
            } else {
                relativePath = "$$relativePath"
            }

            resultSet.add(GdNodeHolder(it, relativePath, uniqueId, tail, "$$hint"))
        }
    }

}
