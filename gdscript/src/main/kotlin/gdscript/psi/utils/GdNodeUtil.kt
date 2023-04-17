package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.model.GdNodeHolder
import gdscript.psi.GdNodePath
import gdscript.utils.VirtualFileUtil.resourcePath
import tscn.index.impl.TscnNodeIndex
import tscn.index.impl.TscnResourceIndex
import tscn.psi.TscnNodeHeader
import tscn.psi.TscnParagraph
import tscn.psi.TscnResourceHeader
import tscn.psi.utils.TscnResourceUtil

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

        return connectedNodes.flatMap { listAvailableNodeForNode(it) }
            .toTypedArray()
    }

    private fun listConnectedNodesForResource(resource: TscnResourceHeader): Iterable<TscnNodeHeader> {
        val path = resource.path

        return PsiTreeUtil.findChildrenOfType(resource.containingFile, TscnNodeHeader::class.java)
            .filter { it.scriptResource == path }
    }

    private fun listAvailableNodeForNode(resourceNode: TscnNodeHeader): Iterable<GdNodeHolder> {
        val scriptPath = resourceNode.scriptResource.split("/")
        val nodes = PsiTreeUtil.findChildrenOfType(resourceNode.containingFile, TscnNodeHeader::class.java)

        return nodes.map {
            // TODO upravit relativní cesty, + rekurze na Instance

            val nodePath = it.nodePath
            val path = nodePath.split("/")

            var relativePath = if (path.size == scriptPath.size) {
                "."
            } else if (path.size > scriptPath.size) {
                path.subList(scriptPath.size, path.size).joinToString("/")
            } else {
                List(scriptPath.size - path.size) { ".." }.joinToString("/")
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

            var uniqueId: String? = null
            if (it.isUniqueNameOwner) {
                uniqueId = "%${it.name}"
                tail = " ${it.name}"
            } else {
                relativePath = "$$relativePath"
            }

            GdNodeHolder(it, relativePath, uniqueId, tail, "$$hint")
        }
    }

    private fun appendAvailableNodes() {

    }

}
