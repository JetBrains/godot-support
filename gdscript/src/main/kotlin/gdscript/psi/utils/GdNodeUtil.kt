package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.impl.GdFileResIndex
import gdscript.model.GdNodeHolder
import gdscript.psi.GdNodePath
import gdscript.utils.StringUtil.camelToSnakeCase
import gdscript.utils.VirtualFileUtil.getPsiFile
import tscn.psi.TscnNodeHeader
import tscn.psi.TscnResourceHeader
import tscn.psi.utils.TscnResourceUtil
import kotlin.io.path.Path
import kotlin.io.path.relativeTo

/**
 * Node utils for available nodes from given script
 */
object GdNodeUtil {

    private val UNQUOTED_NODE_NAME = Regex("[A-Za-z_][A-Za-z0-9_]*")

    fun needsQuotes(name: String): Boolean = !UNQUOTED_NODE_NAME.matches(name)

    fun quoteIfNeeded(name: String): String = if (needsQuotes(name)) "\"$name\"" else name

    fun nodeNameToIdentifier(name: String): String {
        // `camelToSnakeCase` already folds spaces, `-` and `.` into `_` and drops a leading `_`, but
        // leaves other punctuation untouched, e.g. `)` and doesn't care about a leading digit.
        val sanitized = name.camelToSnakeCase()
            .map { if (it.isLetterOrDigit() || it == '_') it else '_' }
            .joinToString("")
            // Make e.g. "c++c++" into "c_c" instead of "c__c__"
            .replace(UNDERSCORE_RUN, "_")
            .trim('_')

        return when {
            sanitized.isEmpty() -> "node"
            sanitized.first().isDigit() -> "_$sanitized"
            else -> sanitized
        }
    }

    private val UNDERSCORE_RUN = Regex("_+")

    /**
     * Returns corresponding node for given NodePath element
     */
    fun findNode(element: GdNodePath): GdNodeHolder? {
        val nodes = listNodes(element)
        val path = element.text
            .trim('$', '%', '"', '^', '\'')
            .split(":")
            .first()

        return nodes.find { it.relativePath.trim('$') == path || it.uniqueId?.trim('%') == path }
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

    fun TscnNodeHeader.relativeOrUniquePath(basePath: String): String {
        if (this.isUniqueNameOwner) {
            return "%${quoteIfNeeded(this.name)}"
        }

        var relativePath = Path(this.nodePath).relativeTo(Path(basePath)).toString().replace("\\", "/")
        if (relativePath.isBlank()) {
            relativePath = "."
        }

        if (relativePath.contains(".")) {
            relativePath = "\"$relativePath\""
        }

        return "$$relativePath"
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
        isInSubscene: Boolean = false,
        visitedFilesSet: MutableSet<PsiFile> = mutableSetOf(),
    ) {
        if (tscnFile in visitedFilesSet) return
        visitedFilesSet += tscnFile

        val nodes = PsiTreeUtil.findChildrenOfType(tscnFile, TscnNodeHeader::class.java)
        val baseName = if (isSingleNode) "" else basePath.split("/").last()

        nodes.forEach {
            // In case of nested instance simply joining paths lead to "Parent/../Child"
            // so skip ".." and remove prefix of it
            var currentNodePath = it.nodePath
            if (parentPath.isNotBlank()) {
                currentNodePath = currentNodePath.removePrefix("..")
            }

            val nodePath = "$parentPath$currentNodePath"

            // Upon encountering a node representing instanced sub scene, we add it in the caller after recursing.
            // That way, we preserve its unique-namedness.
            // Adding the root of the sub scene in the recursive call would duplicate!
            val isInstancedSubSceneRoot = parentPath.isNotBlank() && nodePath == parentPath
            if (isInstancedSubSceneRoot) return@forEach

            var type: String? = null
            val instancePath = it.instanceResource
            if (instancePath.isNotBlank()) {
                val instanceFile = GdFileResIndex.getFiles(instancePath, tscnFile.project)
                    .firstOrNull()
                    ?.getPsiFile(tscnFile.project)
                if (instanceFile != null) {
                    // the instancing header carries no type=, the sub scene root holds the real one
                    type = PsiTreeUtil.findChildrenOfType(instanceFile, TscnNodeHeader::class.java)
                        .firstOrNull { root -> root.parentPath.isEmpty() }
                        ?.type

                    availableNodes(instanceFile, basePath, resultSet, isSingleNode, nodePath, true, visitedFilesSet)
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
            if (!isInSubscene && it.isUniqueNameOwner) {
                uniqueId = "%${it.name}"
                tail = " ${it.name}"
            } else {
                relativePath = "$$relativePath"
            }

            resultSet.add(
                GdNodeHolder(
                    it,
                    relativePath,
                    uniqueId,
                    tail,
                    "$$hint",
                    it.scriptResource.ifEmpty { null },
                    it.nodePath,
                    type ?: it.type,
                )
            )
        }
    }

}
