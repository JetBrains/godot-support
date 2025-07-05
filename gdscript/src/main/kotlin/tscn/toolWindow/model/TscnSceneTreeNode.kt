package tscn.toolWindow.model

import gdscript.psi.utils.GdNodeUtil.relativeOrUniquePath
import tscn.psi.TscnNodeHeader
import javax.swing.tree.DefaultMutableTreeNode

class TscnSceneTreeNode : DefaultMutableTreeNode {

    var myName = ""
    var myType = ""
    var inherited = false
    var basePath = ""
    var hasScript = false
    var hasUniqueName = false
    var isInstance = false
    var resource = ""
    var visible = true
    var parentVisible = true

    constructor(basePath: String) {
        this.basePath = basePath
    }

    constructor(
        node: TscnNodeHeader,
        basePath: String,
        externalType: String? = null,
        inherited: Boolean = false
    ) {
        myName = node.name
        myType = externalType ?: node.type
        userObject = node.relativeOrUniquePath(basePath)
        hasScript = node.hasScript()
        hasUniqueName = node.isUniqueNameOwner
        this.inherited = inherited
        isInstance = node.instanceResource.isNotBlank()
        resource = node.instanceResource.ifEmpty { node.scriptResource }
        visible = node.isVisible
    }

    fun addNodeChild(node: TscnNodeHeader, externalType: String? = null, inherited: Boolean = false) {
        val name = node.name
        if (name.isBlank()) return
        val basePath = (root as TscnSceneTreeNode).basePath

        when (node.parentPath) {
            "" -> {
                myName = name
                myType = externalType ?: node.type
                hasScript = node.hasScript()
                hasUniqueName = node.isUniqueNameOwner
                resource = node.instanceResource.ifEmpty { node.scriptResource }
                visible = node.isVisible
            }

            "." -> {
                insertInner(TscnSceneTreeNode(node, basePath, externalType, inherited), node.index)
            }

            else -> {
                addNodeChild(node, basePath, externalType, node.index, node.parentPath.split('/'), inherited)
            }
        }
    }

    fun addNodeChild(
        node: TscnNodeHeader,
        basePath: String,
        externalType: String?,
        index: Int,
        paths: List<String>,
        inherited: Boolean = false,
    ) {
        if (paths.isEmpty()) {
            insertInner(TscnSceneTreeNode(node, basePath, externalType, inherited), index)
        } else {
            val lookFor = paths.first()
            children
                ?.find { it is TscnSceneTreeNode && it.myName == lookFor }
                ?.let {
                    (it as TscnSceneTreeNode).addNodeChild(
                        node,
                        basePath,
                        externalType,
                        index,
                        paths.subList(1, paths.size),
                        inherited,
                    )
                }
        }
    }

    fun listActions(): List<String> {
        return arrayOf(
            if (hasUniqueName) "unique" else "",
            if (isInstance) "instance" else "",
            if (hasScript) "script" else "",
            "visible",
        ).filter { it.isNotBlank() }
    }

    private fun insertInner(node: TscnSceneTreeNode, index: Int) {
        node.parentVisible = visible
        while (index > 0 && childCount < index) {
            add(TscnSceneTreeNodePlaceholder())
        }

        val i = children?.drop(index.coerceAtLeast(0))?.indexOfFirst { it is TscnSceneTreeNodePlaceholder } ?: -1
        if (i >= 0) {
            children[i] = node
        } else if (index >= 0) {
            insert(node, index)
        } else {
            add(node)
        }
    }

}
