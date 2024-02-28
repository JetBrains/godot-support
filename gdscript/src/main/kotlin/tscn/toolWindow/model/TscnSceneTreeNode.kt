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

    constructor(basePath: String) {
        this.basePath = basePath
    }

    constructor(node: TscnNodeHeader, basePath: String, externalType: String? = null) {
        myName = node.name
        myType = externalType ?: node.type
        userObject = node.relativeOrUniquePath(basePath)
        hasScript = node.hasScript()
        hasUniqueName = node.isUniqueNameOwner
    }

    fun addNodeChild(node: TscnNodeHeader, externalType: String? = null) {
        val name = node.name
        if (name.isBlank()) return
        val basePath = (root as TscnSceneTreeNode).basePath

        when (node.parentPath) {
            "" -> {
                myName = name
                myType = externalType ?: node.type
                hasScript = node.hasScript()
                hasUniqueName = node.isUniqueNameOwner
            }
            "." -> {
                insertInner(TscnSceneTreeNode(node, basePath, externalType), 0.coerceAtLeast(node.index))
            }
            else -> {
                addNodeChild(node, basePath, externalType, 0.coerceAtLeast(node.index), node.parentPath.split('/'))
            }
        }
    }

    fun addNodeChild(node: TscnNodeHeader, basePath: String, externalType: String?, index: Int, paths: List<String>) {
        if (paths.isEmpty()) {
            insertInner(TscnSceneTreeNode(node, basePath, externalType), index)
        } else {
            val lookFor = paths.first()
            (children)
                .find { it is TscnSceneTreeNode && it.myName == lookFor }
                ?.let { (it as TscnSceneTreeNode).addNodeChild(node, basePath, externalType, index, paths.subList(1, paths.size)) }
        }
    }

    private fun insertInner(node: TscnSceneTreeNode, index: Int) {
        if (childCount > index) {
            val i = children.indexOfFirst { it is TscnSceneTreeNodePlaceholder }
            if (i >= 0) {
                node.inherited = true
                children[i] = node
            }
            return
        }

        if (childCount < index) {
            add(TscnSceneTreeNodePlaceholder())
        }
        insert(node, index)
    }

}
